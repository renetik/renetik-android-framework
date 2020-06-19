package renetik.android.task

import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.hasItems

class CSRunConsolidator(private val miliseconds: Int) {

    private val runnables = list<() -> Unit>()
    private var isRunning = false

    operator fun invoke(runnable: () -> Unit) {
        if (isRunning) runnables.add(runnable)
        else {
            runnable()
            isRunning = true
            later(miliseconds) { run() }
        }
    }

    fun run() {
        if (runnables.hasItems) {
            runnables.deleteLast()!!.invoke()
            later(miliseconds) { run() }
        } else isRunning = false
    }

}
