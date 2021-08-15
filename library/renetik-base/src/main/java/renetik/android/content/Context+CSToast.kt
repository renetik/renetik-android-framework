package renetik.android.content

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import renetik.android.framework.CSApplication.Companion.application

object CSToast {
    fun toast(text: String) = Toast.makeText(application, text, Toast.LENGTH_LONG).show()
}

enum class CSToastLength(val value: Int) {
    LongTime(Toast.LENGTH_LONG), ShortTime(LENGTH_SHORT)
}

fun Context.toast(text: String, time: CSToastLength = CSToastLength.LongTime) =
    Toast.makeText(this, text, time.value).show()
