package renetik.android.controller.base.dialog

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.core.kotlin.then
import renetik.android.core.lang.variable.setFalse

fun <T : CSNavigationWindow<*>> T.selectedButton(button: View) = apply {
    button.isSelected = true
    onDismiss { button.isSelected = false }
}

fun <T : CSNavigationWindow<*>> T.show(animation: DialogAnimation = Fade) = apply {
    this.animation = animation
    navigation!!.push(this)
    updateVisibility()
}

fun <T : CSNavigationWindow<*>> T.center(width: Int? = null, height: Int? = null) = apply {
    isFullscreenNavigationItem.setFalse()
    animation = Fade
    dialogContent.updateLayoutParams<FrameLayout.LayoutParams> {
        gravity = Gravity.CENTER
        width?.let { this.width = it }
        height?.let { this.height = it }
    }
}

fun CSNavigationWindow<*>.dismiss() = then { navigation?.pop(this) }

fun <T : CSNavigationWindow<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationWindow<*>> T.isClicksBlocked get() = dialogContent.isClickable
