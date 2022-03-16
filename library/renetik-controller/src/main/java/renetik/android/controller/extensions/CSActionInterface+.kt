package renetik.android.controller.extensions

import android.view.View
import android.view.ViewGroup
import renetik.android.controller.base.dialog.CSNavigationDialog
import renetik.android.controller.base.dialog.DialogAnimation
import renetik.android.framework.event.CSActionInterface
import renetik.android.framework.event.CSRegistration
import renetik.android.framework.event.property.onTrue
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.view.onClick

fun CSActionInterface.dialog(
    dialog: CSNavigationDialog<ViewGroup>,
    animation: DialogAnimation = DialogAnimation.Slide): CSRegistration {
    dialog.onDismiss { setFalse() }
    if (isTrue) dialog.show(DialogAnimation.None)
    return onTrue { dialog.show(animation) }
}

fun CSActionInterface.dialog(
    function: () -> CSNavigationDialog<*>): CSRegistration {
    fun show() {
        val dialog = function()
        dialog.onDismiss { setFalse() }
    }
    if (isTrue) show()
    return onTrue { show() }
}

fun View.actionDialog(
    action: CSActionInterface,
    function: () -> CSNavigationDialog<*>): CSRegistration {
    onClick(action)
    return action.dialog(function)
}