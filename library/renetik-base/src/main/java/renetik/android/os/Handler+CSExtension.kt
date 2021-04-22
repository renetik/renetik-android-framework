package renetik.android.os

import android.os.Handler
import android.os.Looper

object CSHandler {
    val main by lazy { Handler(Looper.getMainLooper()) }
}