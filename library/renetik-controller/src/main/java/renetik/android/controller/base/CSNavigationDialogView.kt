package renetik.android.controller.base

import android.view.Gravity.*
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.content.color
import renetik.android.content.dpToPixel
import renetik.android.content.dpToPixelF
import renetik.android.controller.R
import renetik.android.controller.base.DialogAnimation.Fade
import renetik.android.controller.base.DialogAnimation.Slide
import renetik.android.controller.common.CSNavigationAnimation.*
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.controller.common.CSNavigationItem
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.listenOnce
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.view.extensions.*

enum class DialogAnimation {
    None, Slide, Fade
}

open class CSNavigationDialogView<ViewType : View>(val layout: CSLayoutRes)
    : CSActivityView<FrameLayout>(navigation,
    layout(R.layout.cs_frame_match)), CSNavigationItem {

    private var animation = Slide
    private val marginDp = 15

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }

    val content by lazy {
        inflate<View>(layout.id).also { it.isClickable = true }
    }

    override fun onViewReady() {
        super.onViewReady()
        view.backgroundColor(color(R.color.cs_dialog_background)).onClick {
            if (cancelOnTouchOut) dismiss()
        }
        view.add(content)
    }

    override fun onRemovedFromParent() {
        super.onRemovedFromParent()
        eventOnDismiss.fire()
    }

    override val pushAnimation
        get() = when (animation) {
            Slide -> SlideInRight
            Fade -> FadeIn
            DialogAnimation.None -> None
        }

    override val popAnimation
        get() = when (animation) {
            Slide -> SlideOutLeft
            Fade -> FadeOut
            DialogAnimation.None -> None
        }

    fun dismiss() {
        navigation.pop(this)
    }

    fun show(animation: DialogAnimation? = null) = apply {
        animation?.let { this.animation = it }
        navigation.push(this)
        updateVisibility()
    }

    fun from(fromView: View) = apply {
        animation = Fade
        content.updateLayoutParams<LayoutParams> { gravity = START or TOP }
        val fromViewLocation = fromView.locationOnScreen
        val statusBarHeight = activity().activityView!!.view.locationOnScreen.y
        val fromViewBottomY = fromViewLocation.y.toFloat() + fromView.height - statusBarHeight
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)

        this.content.hasSize {
            var desiredX = fromViewTopCenterX.toFloat() - (this.content.width / 2)
            if (desiredX + this.content.width > displayWidth - dpToPixelF(marginDp))
                desiredX -= (desiredX + this.content.width) - (displayWidth - dpToPixelF(marginDp))
            if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
            this.content.x = desiredX
            this.content.y = fromViewBottomY
        }
        view.backgroundColor(color(R.color.cs_dialog_popup_background))
    }

    fun center() = apply {
        animation = Fade
        content.updateLayoutParams<LayoutParams> { gravity = CENTER }
    }

    fun margin(margin: Int) = apply {
        content.updateLayoutParams<LayoutParams> {
            topMargin = dpToPixel(margin)
            bottomMargin = dpToPixel(margin)
            leftMargin = dpToPixel(margin)
            rightMargin = dpToPixel(margin)
        }
    }

    open fun fullScreen() = apply {
        animation = Slide
        content.updateLayoutParams<LayoutParams> {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }
}