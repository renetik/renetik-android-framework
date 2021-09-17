package renetik.android.framework.util

import android.os.Handler
import android.os.Looper
import renetik.android.framework.logging.CSLog.error
import renetik.kotlin.exception

object CSHandler {
    val main by lazy { Handler(Looper.getMainLooper()) }

    fun post(function: () -> Unit) {
        if (!main.post(function))
            error(exception("Runnable not run"))
    }

    fun post(delay: Long, function: () -> Unit) {
        if (!main.postDelayed(function, delay))
            error(exception("Runnable not run"))
    }

    fun post(delay: Int, function: () -> Unit) {
        post(delay.toLong(), function)
    }

    fun removePosted(function: () -> Unit) {
        main.removeCallbacks(function)
    }
}

