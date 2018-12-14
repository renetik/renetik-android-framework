package renetik.android.extensions

import android.content.Context
import android.widget.Toast
import renetik.android.model.application
import renetik.android.view.CSDialog

fun toast(text: String) = Toast.makeText(application, text, Toast.LENGTH_LONG).show()
fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun Context.toast(text: String, time: Int) = Toast.makeText(this, text, time).show()

fun dialog() = CSDialog(application)
fun Context.dialog() = CSDialog(this)
fun Context.dialog(message: String) = dialog().message(message)
fun Context.dialog(title: String, message: String) = dialog().title(title).message(message)