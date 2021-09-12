package renetik.android.framework.task

import android.os.Handler
import android.os.HandlerThread
import renetik.android.framework.task.CSDoLaterObject.later

object CSBackground {

    fun background(function: () -> Unit) = backgroundHandler.post(function)

    fun background(delay: Int = 0, function: () -> Unit) =
        backgroundHandler.postDelayed(function, delay.toLong())

    val backgroundHandler: Handler by lazy { Handler(backgroundThread.looper) }

    val backgroundThread: HandlerThread by lazy {
        HandlerThread("CSBackground HandlerThread").apply {
            setUncaughtExceptionHandler { _, e -> later { throw RuntimeException(e) } }
            start()
        }
    }
}

