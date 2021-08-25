package renetik.android.controller.base

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import renetik.android.content.color
import renetik.android.content.dpToPixel
import renetik.android.content.dpToPixelF
import renetik.android.controller.R
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.listenOnce
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.view.extensions.*

open class CSMainControllerDialogView<ViewType : View>(
    parent: CSActivityView<*>, val layout: CSLayoutRes) : CSActivityView<ViewType>(parent, layout) {

    val marginDp = 15

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }

    private val backgroundView = FrameLayout(context)
        .backgroundColor(color(R.color.cs_dialog_background)).onClick {
            if (cancelOnTouchOut) dismiss(animated = true)
        }

    override fun obtainView(): ViewType = backgroundView.inflate(layout.id)

    fun dismiss(animated: Boolean = true) {
        if (animated) backgroundView.fadeOut { dismiss() }
        else dismiss()
    }

    private fun dismiss() {
        if (isDialogShown) return
        backgroundView.removeFromSuperview()
        lifecycleStop()
        eventOnDismiss.fire()
    }

    private val isDialogShown get() = backgroundView.parent == null

    open fun show() = apply {
        view.isClickable = true
        activity().controller?.view?.add(backgroundView, layoutMatch)
        backgroundView.hide().fadeIn()
        backgroundView.add(view)
    }

    fun from(fromView: View) = apply {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            gravity = Gravity.START or Gravity.TOP
        }
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
        backgroundView.backgroundColor(color(R.color.cs_dialog_popup_background))
    }

    fun center() = apply {
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
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }
}