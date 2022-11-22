package renetik.android.controller.base.dialog

import android.view.View
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.core.kotlin.then

fun CSNavigationDialog<*>.selected(button: View) = apply {
    button.isSelected = true
    onDismiss { button.isSelected = false }
}

fun <T : CSNavigationDialog<*>> T.show(animation: DialogAnimation = Fade) = apply {
    this.animation = animation
    navigation!!.push(this)
    updateVisibility()
}

fun CSNavigationDialog<*>.dismiss() = then { navigation?.pop(this) }

fun <T : CSNavigationDialog<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationDialog<*>> T.isClicksBlocked get() = dialogContent.isClickable
