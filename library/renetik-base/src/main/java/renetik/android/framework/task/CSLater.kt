package renetik.android.framework.task

import renetik.android.framework.util.CSHandler.post
import renetik.android.framework.util.CSHandler.removePosted

object CSDoLaterObject {

    fun later(delayMilliseconds: Int = 0, function: () -> Unit) =
        CSDoLater(function, delayMilliseconds)

    fun later(function: () -> Unit) = CSDoLater(function)
}

class CSDoLater {
    private val function: () -> Unit

    constructor(function: () -> Unit, delayMilliseconds: Int) {
        this.function = function
        post(delayMilliseconds, function)
    }

    constructor(function: () -> Unit) {
        this.function = function
        post(function)
    }

    fun stop() = removePosted(function)
}
