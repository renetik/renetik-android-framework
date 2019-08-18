package renetik.android.extensions

import android.content.Intent
import android.net.Uri

fun Intent.toOpenUrl(url: String) = apply {
    action = Intent.ACTION_VIEW
    data = Uri.parse(url)
}

val Intent.asString: String
    get() {
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