package renetik.android.controller.base

import android.view.Gravity.*
import android.view.View
import android.view.ViewGroup
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
import renetik.android.controller.base.DialogPopupSide.Bottom
import renetik.android.controller.base.DialogPopupSide.Right
import renetik.android.controller.common.CSNavigationAnimation.*
import renetik.android.controller.common.CSNavigationItem
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.listenOnce
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.framework.lang.property.setFalse
import renetik.android.framework.lang.property.setTrue
import renetik.android.view.extensions.add
import renetik.android.view.extensions.backgroundColor
import renetik.android.view.extensions.locationOnScreen
import renetik.android.view.hasSize
import renetik.android.view.onClick

enum class DialogAnimation {
    None, Slide, Fade
}

enum class DialogPopupSide {
    Bottom, Right
}

open class CSNavigationDialogWindow<ViewType : View>(
    parent: CSActivityView<out ViewGroup>, val layout: CSLayoutRes)
    : CSActivityView<FrameLayout>(parent.navigation!!,
    layout(R.layout.cs_frame_match)), CSNavigationItem {

    override var isFullscreen: CSEventProperty<Boolean> = property(false)
    private var animation = Slide
    private val marginDp = 7

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }

    private val dialogContent by lazy {
        inflate<ViewType>(layout.id).also { it.isClickable = true }
    }

    override fun onViewReady() {
        super.onViewReady()
        view.backgroundColor(color(R.color.cs_dialog_background)).onClick {
            if (cancelOnTouchOut) dismiss()
        }
        view.add(dialogContent)
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

    fun dismiss() = navigation!!.pop(this)

    fun show(animation: DialogAnimation? = null) = apply {
        animation?.let { this.animation = it }
        navigation!!.push(this)
        updateVisibility()
    }

    fun from(fromView: View, side: DialogPopupSide = Bottom) = apply {
        fromView.isSelected = true
        eventOnDismiss.listenOnce { fromView.isSelected = false }
        isFullscreen.setFalse()
        animation = Fade

        dialogContent.updateLayoutParams<LayoutParams> { gravity = START or TOP }
        dialogContent.hasSize {
            if (side == Bottom) positionDialogContentFromViewBottom(fromView)
            else if (side == Right) positionDialogContentFromViewRight(fromView)
        }

        view.backgroundColor(color(R.color.cs_dialog_popup_background))
    }

    override val statusBarHeight = activity().activityView!!.view.locationOnScreen.y

    private fun positionDialogContentFromViewBottom(fromView: View) {
        val fromViewLocation = fromView.locationOnScreen
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (dialogContent.width / 2)
        if (desiredX + dialogContent.width > displayWidth - dpToPixelF(marginDp))
            desiredX -= (desiredX + dialogContent.width) -
                    (displayWidth - dpToPixelF(marginDp))
        if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
        dialogContent.x = desiredX
        dialogContent.y = fromViewLocation.y.toFloat() + fromView.height - statusBarHeight
    }

    private fun positionDialogContentFromViewRight(fromView: View) {
        val fromViewLocation = fromView.locationOnScreen
        val fromViewLeftCenterY = fromViewLocation.y + (fromView.height / 2)
        var desiredY = fromViewLeftCenterY.toFloat() - (dialogContent.height / 2)
        if (desiredY + dialogContent.height > displayHeight - dpToPixelF(marginDp))
            desiredY -= (desiredY + dialogContent.height) -
                    (displayHeight - dpToPixelF(marginDp) - statusBarHeight)
        if (desiredY < dpToPixelF(marginDp)) desiredY = dpToPixelF(marginDp)
        dialogContent.x = fromViewLocation.x.toFloat() + fromView.width
        dialogContent.y = desiredY
    }

    fun center() = apply {
        isFullscreen.setFalse()
        animation = Fade
        dialogContent.updateLayoutParams<LayoutParams> { gravity = CENTER }
    }

    // TODO not used .. useful ? not ? remove.
    fun margin(margin: Int) = apply {
        dialogContent.updateLayoutParams<LayoutParams> {
            topMargin = dpToPixel(margin)
            bottomMargin = dpToPixel(margin)
            leftMargin = dpToPixel(margin)
            rightMargin = dpToPixel(margin)
        }
    }

    open fun fullScreen() = apply {
        isFullscreen.setTrue()
        animation = Slide
        dialogContent.updateLayoutParams<LayoutParams> {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }
}