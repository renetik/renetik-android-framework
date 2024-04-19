package renetik.android.controller.navigation

import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.annotation.LayoutRes
import androidx.core.view.updateLayoutParams
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.height
import renetik.android.controller.extensions.width
import renetik.android.controller.navigation.CSNavigationItemAnimation.Fade
import renetik.android.controller.navigation.CSNavigationItemAnimation.None
import renetik.android.controller.navigation.CSNavigationItemAnimation.Slide
import renetik.android.controller.navigation.CSNavigationItemAnimation.SlideFade
import renetik.android.controller.navigation.CSNavigationItemPopupSide.Bottom
import renetik.android.controller.navigation.CSNavigationItemPopupSide.Right
import renetik.android.controller.navigation.CSNavigationItemPopupSide.Top
import renetik.android.core.extensions.content.color
import renetik.android.core.kotlin.primitives.dpf
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.logging.CSLog.logErrorTrace
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.destruct
import renetik.android.event.fire
import renetik.android.event.listen
import renetik.android.event.registration.plus
import renetik.android.event.registration.registerListenOnce
import renetik.android.ui.R.color
import renetik.android.ui.R.layout.cs_frame_match
import renetik.android.ui.extensions.registerAfterLayout
import renetik.android.ui.extensions.registerHasSize
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.background
import renetik.android.ui.extensions.view.bottomFloat
import renetik.android.ui.extensions.view.heightWrap
import renetik.android.ui.extensions.view.leftFloat
import renetik.android.ui.extensions.view.locationInWindow
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.onViewLayout
import renetik.android.ui.extensions.view.passClicksUnder
import renetik.android.ui.extensions.view.rightFloat
import renetik.android.ui.extensions.view.topFloat

open class CSNavigationItemView(
    val navigationParent: CSActivityView<out ViewGroup>,
    @LayoutRes private val viewLayout: Int,
    @LayoutRes private val frameLayout: Int? = null,
    @LayoutRes private val fullScreenFrameLayout: Int? = null,
) : CSActivityView<FrameLayout>(
    navigationParent.navigation!!, cs_frame_match.layout
), CSNavigationItem {

    constructor(
        parent: CSActivityView<out ViewGroup>,
        @LayoutRes viewLayout: Int,
        @LayoutRes frameLayout: Int? = null
    ) : this(parent, viewLayout, frameLayout, null)

    constructor(
        parent: CSActivityView<out ViewGroup>,
        @LayoutRes viewLayout: Int,
    ) : this(parent, viewLayout, null, null)

    val viewContent: View by lazy {
        val frameLayout = if (isFullScreen) (fullScreenFrameLayout ?: frameLayout) else frameLayout
        (frameLayout?.let { inflate<FrameLayout>(it).apply { add<View>(viewLayout) } }
            ?: inflate<View>(viewLayout))
    }

    final override var isFullScreen = false

    var isPopup = false
        private set

    var animation = Fade
    private val contentMarginDp = 9

    private val eventDismiss = event()
    fun onDismiss(function: () -> Unit) = eventDismiss.listen(function)

    private val eventOnClose = event()
    fun onClose(function: () -> Unit) = eventOnClose.listen(function)

    private var dismissOnTouchOut = false
    fun dismissOnTouchOut(dismiss: Boolean = true) = apply { dismissOnTouchOut = dismiss }

    private var passClicksUnder = false
    fun passClicksUnder(pass: Boolean = true) = apply {
        passClicksUnder = pass
        view.passClicksUnder(passClicksUnder)
    }

    init {
        registerListenOnce(navigationParent.eventDestruct) {
            if (!isShowingInPager && lifecycleStopOnRemoveFromParentView) logErrorTrace { "Unexpected but don't know why now..." }
            if (isShowingInPager) close()
            if (!lifecycleStopOnRemoveFromParentView) destruct()
        }
    }

    override fun onViewReady() {
        super.onViewReady()
        view.background(color(color.cs_dialog_background))
        view.add(viewContent)
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        if (dismissOnTouchOut) view.onClick { onBackgroundClick() }
        else view.passClicksUnder(passClicksUnder)
    }

    protected open fun onBackgroundClick() = dismiss()

    fun dismiss() {
        eventDismiss.fire()
        close()
    }

    private fun close() {
        navigation?.pop(this)
    }

    override fun onRemovedFromParentView() {
        super.onRemovedFromParentView()
        eventOnClose.fire()
    }

    override val pushAnimation
        get() = when (animation) {
            Slide, SlideFade -> CSNavigationAnimation.SlideInRight
            Fade -> CSNavigationAnimation.FadeIn
            None -> CSNavigationAnimation.None
        }

    override val popAnimation
        get() = when (animation) {
            Slide -> CSNavigationAnimation.SlideOutLeft
            Fade, SlideFade -> CSNavigationAnimation.FadeOut
            None -> CSNavigationAnimation.None
        }

    fun from(button: View, side: CSNavigationItemPopupSide = Bottom) = apply {
        isPopup = true
        selected(button)
        dismissOnTouchOut(true)
        isFullScreen = false
        animation = Fade
        viewContent.updateLayoutParams<LayoutParams> { gravity = START or TOP }
        registerHasSize {
            when (side) {
                Bottom -> positionDialogContentFromViewBottom(button)
                Right -> positionDialogContentFromViewRight(button)
                Top -> positionDialogContentFromViewTop(button)
            }
            correctContentOverflow()
        }
        this + view.onViewLayout(::correctContentOverflow)
        view.background(color(color.cs_dialog_popup_background))
    }

    private fun positionDialogContentFromViewBottom(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (viewContent.width / 2)
        if (desiredX + viewContent.width > width - contentMarginDp.dpf) desiredX -= (desiredX + viewContent.width) - (width - contentMarginDp.dpf)
        if (desiredX < contentMarginDp.dpf) desiredX = contentMarginDp.dpf
        viewContent.x = desiredX
        viewContent.y = fromViewLocation.y.toFloat() + fromView.height
    }

    private val screenAvailableHeight get() = height - contentMarginDp.dpf * 2
    private val screenAvailableWidth get() = width - contentMarginDp.dpf * 2

    private fun positionDialogContentFromViewTop(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (viewContent.width / 2)
        if (desiredX + viewContent.width > screenAvailableWidth) desiredX -= (desiredX + viewContent.width) - screenAvailableWidth
        if (desiredX < contentMarginDp.dpf) desiredX = contentMarginDp.dpf
        viewContent.x = desiredX
        viewContent.y = fromViewLocation.y.toFloat() - viewContent.height - contentMarginDp.dpf
    }

    private fun correctContentOverflow() {
        if (viewContent.bottomFloat > screenAvailableHeight) viewContent.topFloat -= viewContent.bottomFloat - screenAvailableHeight
        if (viewContent.rightFloat > screenAvailableWidth) viewContent.leftFloat -= viewContent.rightFloat - screenAvailableWidth
    }

    private fun positionDialogContentFromViewRight(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewLeftCenterY = fromViewLocation.y + (fromView.height / 2)
        var desiredY = fromViewLeftCenterY.toFloat() - (viewContent.height / 2)
        if (desiredY + viewContent.height > screenAvailableHeight) desiredY -= (desiredY + viewContent.height) - screenAvailableHeight
        if (desiredY < contentMarginDp.dpf) desiredY = contentMarginDp.dpf
        viewContent.x = fromViewLocation.x.toFloat() + fromView.width
        viewContent.y = desiredY
    }

    fun wrapContentIfNotFullscreen() {
        if (isFullScreen) return
        viewContent.heightWrap()
        registerAfterLayout(::correctContentOverflow)
    }
}