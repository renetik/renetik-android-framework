package renetik.android.dialog.extensions

import android.app.Activity
import renetik.android.base.CSView
import renetik.android.dialog.CSDialog

fun Activity.dialog() = CSDialog(this)
fun CSView<*>.dialog() = CSDialog(this)

fun CSView<*>.dialog(message: String) = dialog().title(message)
fun CSView<*>.dialog(title: String, message: String) = dialog().title(title).message(message)