package renetik.android.framework.task

import renetik.android.framework.util.CSMainHandler.postOnMain
import renetik.android.framework.util.CSMainHandler.removePosted

class CSDoLater {
    companion object {
        fun later(delayMilliseconds: Int = 0, function: () -> Unit) =
            CSDoLater(function, delayMilliseconds)

        fun later(function: () -> Unit) = CSDoLater(function)
    }

    private val function: () -> Unit

    constructor(function: () -> Unit, delayMilliseconds: Int) {
        this.function = function
        postOnMain(delayMilliseconds, function)
    }

    constructor(function: () -> Unit) {
        this.function = function
        postOnMain(function)
    }

    fun stop() = removePosted(function)
}
