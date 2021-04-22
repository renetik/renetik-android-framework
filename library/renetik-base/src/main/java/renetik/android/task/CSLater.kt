package renetik.android.task

import renetik.android.java.extensions.exception
import renetik.android.logging.CSLog.logError
import renetik.android.os.CSHandler.main

object CSDoLaterObject {

    fun later(delayMilliseconds: Int = 0, function: () -> Unit): CSDoLater {
        return CSDoLater(function, delayMilliseconds)
    }

    fun later(function: () -> Unit) = CSDoLater(function)
}

class CSDoLater {
    private val function: () -> Unit

    constructor(function: () -> Unit, delayMilliseconds: Int) {
        this.function = function
        if (!main.postDelayed(this.function, delayMilliseconds.toLong()))
            logError(exception("Runnable not run"))
    }

    constructor(function: () -> Unit) {
        this.function = function
        if (!main.post(this.function)) logError(exception("Runnable not run"))
    }

    fun stop() = main.removeCallbacks(function)
}
