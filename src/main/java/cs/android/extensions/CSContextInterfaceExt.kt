package cs.android.extensions

import cs.android.CSContextInterface
import cs.android.view.CSDialog

fun CSContextInterface.dialog(title: String, onPositive: () -> Unit) =
        dialog(title).action { onPositive() }

fun CSContextInterface.dialog() = CSDialog(context())

fun CSContextInterface.dialog(title: String) = dialog().message(title)

fun CSContextInterface.dialog(title: String, message: String) =
        dialog().title(title).message(message)
