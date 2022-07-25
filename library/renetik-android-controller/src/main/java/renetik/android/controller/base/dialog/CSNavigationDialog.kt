package renetik.android.controller.base.dialog

import android.view.Gravity.*
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.dialog.DialogAnimation.*
import renetik.android.controller.base.dialog.DialogPopupSide.Bottom
import renetik.android.controller.base.dialog.DialogPopupSide.Right
import renetik.android.controller.base.hasSize
import renetik.android.controller.common.CSNavigationAnimation.*
import renetik.android.controller.common.CSNavigationAnimation.None
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.height
import renetik.android.controller.extensions.width
import renetik.android.core.extensions.content.color
import renetik.android.core.extensions.content.dpToPixelF
import renetik.android.core.extensions.content.statusBarHeight
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.onDestroy
import renetik.android.event.fire
import renetik.android.event.listen
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.register
import renetik.android.ui.R.color
import renetik.android.ui.R.layout.cs_frame_match
import renetik.android.ui.extensions.view.*
import java.io.Closeable

open class CSNavigationDialog<ViewType : View>(
    val parent: CSActivityView<out ViewGroup>, layout: CSLayoutRes)
    : CSActivityView<FrameLayout>(parent.navigation!!, layout(cs_frame_match)),
    CSNavigationItem, Closeable {

    val dialogContent: ViewType = inflate(layout.id)
    override var isFullscreenNavigationItem = property(false)
    var animation = Fade
    private val marginDp = 5

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listen(function)

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }

    init {
        dialogContent.isClickable = true
        register(parent.onDestroy { dismiss() })
    }

    override fun onViewReady() {
        super.onViewReady()
        view.background(color(color.cs_dialog_background))
            .onClick { if (cancelOnTouchOut) dismiss() }
        view.add(dialogContent)
    }

    override fun onRemovedFromParentView() {
        super.onRemovedFromParentView()
        eventOnDismiss.fire()
    }

    override val pushAnimation
        get() = when (animation) {
            Slide, SlideFade -> SlideInRight
            Fade -> FadeIn
            DialogAnimation.None -> None
        }

    override val popAnimation
        get() = when (animation) {
            Slide -> SlideOutLeft
            Fade, SlideFade -> FadeOut
            DialogAnimation.None -> None
        }

    fun from(fromView: View, side: DialogPopupSide = Bottom) = apply {
        pressed(fromView)
        isFullscreenNavigationItem.setFalse()
        animation = Fade
        dialogContent.updateLayoutParams<LayoutParams> { gravity = START or TOP }
        hasSize {
            if (side == Bottom) positionDialogContentFromViewBottom(fromView)
            else if (side == Right) positionDialogContentFromViewRight(fromView)
            correctHeight()
        }
        view.background(color(color.cs_dialog_popup_background))
    }

    private fun positionDialogContentFromViewBottom(fromView: View) {
        val fromViewLocation = fromView.locationOnScreen
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (dialogContent.width / 2)
        if (desiredX + dialogContent.width > width - dpToPixelF(marginDp))
            desiredX -= (desiredX + dialogContent.width) - (width - dpToPixelF(marginDp))
        if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
        dialogContent.x = desiredX

        dialogContent.y = fromViewLocation.y.toFloat() + fromView.height - statusBarHeight
    }

    private fun correctHeight() {
        if (dialogContent.y + dialogContent.height > height - dpToPixelF(marginDp))
            dialogContent.height(height - statusBarHeight - dpToPixelF(marginDp) - dialogContent.y)
    }

    private fun positionDialogContentFromViewRight(fromView: View) {
        val fromViewLocation = fromView.locationOnScreen
        val fromViewLeftCenterY = fromViewLocation.y + (fromView.height / 2)
        var desiredY = fromViewLeftCenterY.toFloat() - (dialogContent.height / 2)
        if (desiredY + dialogContent.height > height - dpToPixelF(marginDp))
            desiredY -= (desiredY + dialogContent.height) -
                    (height - dpToPixelF(marginDp) - statusBarHeight)
        if (desiredY < dpToPixelF(marginDp)) desiredY = dpToPixelF(marginDp)
        dialogContent.x = fromViewLocation.x.toFloat() + fromView.width
        dialogContent.y = desiredY
    }

    fun center() = this.center(null, null)

    fun center(width: Int? = null, height: Int? = null) = apply {
        isFullscreenNavigationItem.setFalse()
        animation = Fade
        dialogContent.updateLayoutParams<LayoutParams> {
            gravity = CENTER
            width?.let { this.width = it }
            height?.let { this.height = it }
        }
    }

    fun fullScreen() = apply {
        isFullscreenNavigationItem.setTrue()
        animation = Slide
        dialogContent.updateLayoutParams<LayoutParams> {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }

    val isFullscreen get() = isFullscreenNavigationItem

    override fun close() = dismiss()
}