package renetik.android.framework.util

import android.os.Handler
import android.os.Looper
import renetik.android.framework.logging.CSLog.error
import renetik.kotlin.exception

object CSMainHandler {
    val handler by lazy { Handler(Looper.getMainLooper()) }

    fun postOnMain(function: () -> Unit) {
        if (!handler.post(function))
            error(exception("Runnable not run"))
    }

    fun postOnMain(delay: Long, function: () -> Unit) {
        if (!handler.postDelayed(function, delay))
            error(exception("Runnable not run"))
    }

    fun postOnMain(delay: Int, function: () -> Unit) {
        postOnMain(delay.toLong(), function)
    }

    fun removePosted(function: () -> Unit) {
        handler.removeCallbacks(function)
    }
}

