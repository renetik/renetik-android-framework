package renetik.android.task

import renetik.android.task.CSDoLaterObject.later

fun repeat(interval: Int, runnable: (CSWork) -> Unit): CSWork {
    return CSWork(interval, runnable).start()
}

class CSWork(private var interval: Int, private val function: (CSWork) -> Unit) {

    private var isStarted = false
    private var doLater: CSDoLater? = null

    fun run() = apply { function(this) }

    fun start() = apply {
        if (!isStarted) {
            isStarted = true
            process()
        }
    }

    fun stop() = apply {
        isStarted = false
        doLater?.stop()
    }

    private fun process() {
        doLater = later(interval) {
            if (this.isStarted) {
                function(this)
                process()
            }
        }
    }

    fun start(start: Boolean) = apply { if (start) start() else stop() }

    fun interval(interval: Int) = apply { this.interval = interval }
}
