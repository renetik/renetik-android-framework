package renetik.android.task

import android.os.Handler
import renetik.android.java.extensions.exception
import renetik.android.logging.CSLog.logError

private lateinit var handler: Handler

fun initializeHandler() {
    handler = Handler()
}

fun doLater(delayMilliseconds: Int, function: () -> Unit): CSDoLater {
    return CSDoLater(function, delayMilliseconds)
}

fun doLater(function: () -> Unit) = CSDoLater(function)

class CSDoLater {

    private val function: () -> Unit

    constructor(function: () -> Unit, delayMilliseconds: Int) {
        this.function = function
        if (!handler.postDelayed(this.function, delayMilliseconds.toLong()))
            logError(exception("Runnable not run"))
    }

    constructor(function: () -> Unit) {
        this.function = function
        if (!handler.post(this.function)) logError(exception("Runnable not run"))
    }

    fun stop() = handler.removeCallbacks(function)

}
