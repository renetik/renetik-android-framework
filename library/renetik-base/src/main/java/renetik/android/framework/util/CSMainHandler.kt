package renetik.android.framework.util

import android.os.Handler
import android.os.Looper.getMainLooper
import renetik.android.framework.logging.CSLog.logWarn

object CSMainHandler {
    val mainHandler by lazy { Handler(getMainLooper()) }

    fun postOnMain(function: () -> Unit) {
        if (!mainHandler.post(function)) logWarn("Runnable not run")
    }

    fun postOnMain(delay: Long, function: () -> Unit) {
        if (!mainHandler.postDelayed(function, delay)) logWarn("Runnable not run")
    }

    fun postOnMain(delay: Int, function: () -> Unit) {
        postOnMain(delay.toLong(), function)
    }

    fun removePosted(function: () -> Unit) {
        mainHandler.removeCallbacks(function)
    }
}

