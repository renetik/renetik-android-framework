package renetik.android.framework.util

import android.os.Handler
import android.os.Looper.getMainLooper
import renetik.kotlin.unexpected

object CSMainHandler {
    val mainHandler by lazy { Handler(getMainLooper()) }

    fun postOnMain(function: () -> Unit) {
        if (!mainHandler.post(function)) unexpected("Runnable not run")
    }

    fun postOnMain(delay: Long, function: () -> Unit) {
        if (!mainHandler.postDelayed(function, delay)) unexpected("Runnable not run")
    }

    fun postOnMain(delay: Int, function: () -> Unit) {
        postOnMain(delay.toLong(), function)
    }

    fun removePosted(function: () -> Unit) {
        mainHandler.removeCallbacks(function)
    }
}

