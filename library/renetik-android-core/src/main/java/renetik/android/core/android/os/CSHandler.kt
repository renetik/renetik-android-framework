package renetik.android.core.android.os

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper.getMainLooper

object CSHandler {
    val threadHandler: Handler by lazy {
        val thread = HandlerThread("CSHandler background")
        thread.start()
        Handler(thread.looper)
    }
    val mainHandler by lazy {
        Handler(getMainLooper())
    }
}