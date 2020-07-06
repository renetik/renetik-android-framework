package renetik.android.task

import android.os.Handler
import android.os.HandlerThread

fun background(function: () -> Unit) = handler.post(function)

private val handler: Handler by lazy { Handler(handlerThread.looper) }

private val handlerThread: HandlerThread by lazy {
    HandlerThread("RenetikBackgroundThread").apply {
        setUncaughtExceptionHandler { _, e -> later { throw RuntimeException(e) } }
        start()
    }
}