package renetik.android.task

import android.os.Handler
import android.os.HandlerThread
import renetik.android.task.CSDoLaterObject.later

object CSBackground {

    fun background(function: () -> Unit) = handler.post(function)
    fun background(delay: Int = 0, function: () -> Unit) =
        handler.postDelayed(function, delay.toLong())

    private val handler: Handler by lazy { Handler(thread.looper) }

    private val thread: HandlerThread by lazy {
        HandlerThread("CSBackgroundThread").apply {
            setUncaughtExceptionHandler { _, e -> later { throw RuntimeException(e) } }
            start()
        }
    }
}

