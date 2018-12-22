package renetik.android.task

import renetik.android.java.collections.list

class CSRunConsolidator(private val miliseconds: Int) {

    private val runnables = list<() -> Unit>()
    private var isRunning = false

    operator fun invoke(runnable: () -> Unit) {
        if (isRunning) runnables.add(runnable)
        else {
            runnable()
            isRunning = true
            doLater(miliseconds) { run() }
        }
    }

    fun run() {
        if (runnables.hasItems) {
            runnables.deleteLast()!!.invoke()
            doLater(miliseconds) { run() }
        } else isRunning = false
    }

}
