package renetik.android.util

import android.util.Log
import renetik.android.java.extensions.asString

fun debug(message: Any) {
    print(message.asString)
}