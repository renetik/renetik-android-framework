package renetik.android.controller.extensions

//fun CSActionInterface.dialog(
//    dialog: CSNavigationDialog<ViewGroup>,
//    animation: DialogAnimation = DialogAnimation.Slide): CSRegistration {
//    dialog.onDismiss { setFalse() }
//    if (isTrue) dialog.show(DialogAnimation.None)
//    return onTrue { dialog.show(animation) }
//}

//fun CSActionInterface.dialog(
//    function: () -> CSNavigationDialog<*>): CSRegistration {
//    fun show() {
//        val dialog = function()
//        dialog.onDismiss { setFalse() }
//    }
//    if (isTrue) show()
//    return onTrue { show() }
//}

//fun View.actionDialog(
//    action: CSActionInterface,
//    function: () -> CSNavigationDialog<*>): CSRegistration {
//    onClick(action)
//    return action.dialog(function)
//}