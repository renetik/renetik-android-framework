package renetik.android.controller.extensions

import renetik.android.framework.CSView
import renetik.android.dialog.CSDialog

fun CSView<*>.dialog() = CSDialog(this.view)
fun CSView<*>.dialog(message: String) = dialog().title(message)
fun CSView<*>.dialog(title: String, message: String) =
    dialog().title(title).message(message)