package renetik.android.controller.base.dialog

import android.view.View
import renetik.android.controller.base.hasParentView
import renetik.android.core.kotlin.primitives.isTrue

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

fun <T : CSNavigationDialog<*>> T.show(animation: DialogAnimation) = apply {
    this.animation = animation
    navigation!!.push(this)
    updateVisibility()
}

fun CSNavigationDialog<*>.dismiss() =
    (!isDestroyed && hasParentView).isTrue { navigation!!.pop(this) }
