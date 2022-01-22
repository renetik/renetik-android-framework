package renetik.android.framework.task

import renetik.kotlin.collections.deleteLast
import renetik.kotlin.collections.hasItems
import renetik.kotlin.collections.list
import renetik.kotlin.later

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
