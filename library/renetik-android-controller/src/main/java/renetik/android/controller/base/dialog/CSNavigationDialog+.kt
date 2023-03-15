package renetik.android.controller.base.dialog

import android.view.View
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.core.kotlin.then

fun <T : CSNavigationWindow<*>> T.selectedButton(button: View) = apply {
    button.isSelected = true
    onDismiss { button.isSelected = false }
}

fun <T : CSNavigationWindow<*>> T.show(animation: DialogAnimation = Fade) = apply {
    this.animation = animation
    navigation!!.push(this)
    updateVisibility()
}

fun CSNavigationWindow<*>.dismiss() = then { navigation?.pop(this) }

fun <T : CSNavigationWindow<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationWindow<*>> T.isClicksBlocked get() = dialogContent.isClickable
