package renetik.android.framework.task

import renetik.android.framework.util.CSMainHandler.postOnMain
import renetik.android.framework.util.CSMainHandler.removePosted

fun repeat(interval: Int, runnable: (CSWork) -> Unit) = CSWork(interval, runnable)

class CSWork(private var interval: Int, private val function: (CSWork) -> Unit) {

    private var isStarted = false

    fun run() = apply { function(this) }

    fun start() = apply {
        if (!isStarted) {
            isStarted = true
            postOnMain(interval, ::runFunction)
        }
    }

    fun stop() = apply {
        isStarted = false
        removePosted(::runFunction)
    }

    private fun runFunction() {
        if (this.isStarted) {
            function(this)
            postOnMain(interval, ::runFunction)
        }
    }

    fun start(start: Boolean) = apply { if (start) start() else stop() }

    fun interval(interval: Int) = apply { this.interval = interval }
}
