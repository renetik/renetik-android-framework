package renetik.android.task

fun schedule(milliseconds: Int, runnable: () -> Unit): CSWork {
    return CSWork(milliseconds, runnable)
}

class CSWork(private var delayMilliseconds: Int, private val workToInvoke: () -> Unit) {

    private var stop = true
    private var doLater: CSDoLater? = null

    val isStarted: Boolean
        get() = !stop

    fun run() = apply { workToInvoke() }

    fun start(start: Boolean) = apply {
        if (start) start()
        else stop()
    }

    fun interval(interval: Int) = apply { delayMilliseconds = interval }

    fun interval() = delayMilliseconds

    fun start() = apply {
        if (stop) {
            stop = false
            process()
        }
    }

    fun stop() = apply {
        stop = true
        doLater?.stop()
    }

    private fun process() {
        doLater = doLater(delayMilliseconds) {
            if (!stop) {
                workToInvoke()
                process()
            }
        }
    }
}
