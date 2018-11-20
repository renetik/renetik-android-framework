package renetik.android.extensions

import android.content.Context
import android.widget.Toast
import renetik.android.model.application

fun toast(text: String) {
    Toast.makeText(application, text, Toast.LENGTH_LONG).show()
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

private fun Context.toast(text: String, time: Int) {
    Toast.makeText(this, text, time).show()
}