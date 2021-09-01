package renetik.android.controller.base

import android.view.Gravity
import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
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
    : CSActivityView<ViewType>(navigation, layout) {

    private var animation = Slide
    private val marginDp = 15

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }


    private val backgroundView = object : CSActivityView<FrameLayout>(navigation,
        layout(R.layout.cs_frame_match)), CSNavigationItem {
        override fun onViewReady() {
            view.backgroundColor(color(R.color.cs_dialog_background)).onClick {
                if (cancelOnTouchOut) dismiss()
            }
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
    }

    override fun obtainView(): ViewType = backgroundView.inflate(layout.id)

    fun dismiss() {
//        if (isDialogShown) return
        navigation.pop(backgroundView)
    }

    private val isDialogShown get() = backgroundView.view.isShowing()

    fun show(animation: DialogAnimation? = null) = apply {
        animation?.let { this.animation = it }
        view.isClickable = true
        navigation.push(backgroundView)
        backgroundView.view.add(view)
        updateVisibility()
    }

    fun from(fromView: View) = apply {
        animation = Fade
        view.updateLayoutParams<FrameLayout.LayoutParams> { gravity = START or TOP }
        val fromViewLocation = fromView.locationOnScreen
        val statusBarHeight = activity().controller!!.view.locationOnScreen.y
        val fromViewBottomY = fromViewLocation.y.toFloat() + fromView.height - statusBarHeight
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)

        this.view.hasSize {
            var desiredX = fromViewTopCenterX.toFloat() - (this.view.width / 2)
            if (desiredX + this.view.width > displayWidth - dpToPixelF(marginDp))
                desiredX -= (desiredX + this.view.width) - (displayWidth - dpToPixelF(marginDp))
            if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
            this.view.x = desiredX
            this.view.y = fromViewBottomY
        }
        backgroundView.view.backgroundColor(color(R.color.cs_dialog_popup_background))
    }

    fun center() = apply {
        animation = Fade
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            gravity = Gravity.CENTER
        }
    }

    fun margin(margin: Int) = apply {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            topMargin = dpToPixel(margin)
            bottomMargin = dpToPixel(margin)
            leftMargin = dpToPixel(margin)
            rightMargin = dpToPixel(margin)
        }
    }

    open fun fullScreen() = apply {
        animation = Slide
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }
}