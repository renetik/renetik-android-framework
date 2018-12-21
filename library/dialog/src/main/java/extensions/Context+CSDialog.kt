package renetik.android.dialog.extensions

import android.content.Context
import renetik.android.dialog.CSDialog
import renetik.android.base.application

fun dialog() = CSDialog(application)
fun Context.dialog() = CSDialog(this)
fun Context.dialog(message: String) = dialog().message(message)
fun Context.dialog(title: String, message: String) = dialog().title(title).message(message)