package renetik.android.task

import android.os.Handler
import android.os.HandlerThread
import renetik.android.task.CSDoLaterObject.later

object CSBackground {

    fun background(function: () -> Unit) = handler.post(function)

    private val handler: Handler by lazy { Handler(thread.looper) }

    private val thread: HandlerThread by lazy {
        HandlerThread("CSBackgroundThread").apply {
            setUncaughtExceptionHandler { _, e -> later { throw RuntimeException(e) } }
            start()
        }
    }
}

