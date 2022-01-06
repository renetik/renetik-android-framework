package renetik.android.controller.extensions

import android.view.View
import android.view.ViewGroup
import renetik.android.controller.base.CSNavigationDialog
import renetik.android.controller.base.DialogAnimation
import renetik.android.framework.event.CSActionInterface
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.property.onTrue
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.view.onClick

fun CSActionInterface.dialog(dialog: CSNavigationDialog<ViewGroup>,
                             animation: DialogAnimation = DialogAnimation.Slide): CSEventRegistration {
    dialog.onDismiss { setFalse() }
    if (isTrue) dialog.show(DialogAnimation.None)
    return onTrue { dialog.show(animation) }
}

fun CSActionInterface.dialog(function: () -> CSNavigationDialog<*>): CSEventRegistration {
    fun show() {
        val dialog = function()
        dialog.onDismiss { setFalse() }
    }
    if (isTrue) show()
    return onTrue { show() }
}

fun View.actionDialog(action: CSActionInterface,
                      function: () -> CSNavigationDialog<*>): CSEventRegistration {
    onClick(action)
    return action.dialog(function)
}