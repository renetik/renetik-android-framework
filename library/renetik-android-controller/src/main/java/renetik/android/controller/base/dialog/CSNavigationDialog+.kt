package renetik.android.controller.base.dialog

import android.view.View
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.core.kotlin.run
import renetik.android.event.registration.later

fun CSNavigationDialog<*>.pressed(button: View) = apply {
    later(50) { button.isPressed = true }
    onDismiss { button.isPressed = false }
}

fun CSNavigationDialog<*>.active(button: View) = apply {
    button.isActivated = true
    onDismiss { button.isActivated = false }
}

fun CSNavigationDialog<*>.selected(button: View) = apply {
    button.isSelected = true
    onDismiss { button.isSelected = false }
}

fun <T : CSNavigationDialog<*>> T.show(animation: DialogAnimation = Fade) = apply {
    this.animation = animation
    navigation!!.push(this)
    updateVisibility()
}

fun CSNavigationDialog<*>.dismiss() = run { navigation?.pop(this) }

fun <T : CSNavigationDialog<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationDialog<*>> T.isClicksBlocked get() = dialogContent.isClickable
