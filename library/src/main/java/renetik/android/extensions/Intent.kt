package renetik.android.extensions

import android.content.Intent

fun Intent.asString(): String {
    val string = StringBuilder(toString())
    extras?.let {
        string.append(" Extras: ")
        for (key in it.keySet()) {
            val info = it.get(key)
            string.append("$key $info (${info?.javaClass?.name ?: ""})")
        }
    }
    return string.toString()
}