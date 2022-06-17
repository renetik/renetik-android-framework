package renetik.android.framework.task

import renetik.android.core.kotlin.collections.deleteLast
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.list
import renetik.android.core.lang.CSMainHandler.postOnMain

class CSRunConsolidator(private val miliseconds: Int) {

    private val runnables = list<() -> Unit>()
    private var isRunning = false

    operator fun invoke(runnable: () -> Unit) {
        if (isRunning) runnables.add(runnable)
        else {
            runnable()
            isRunning = true
            postOnMain(miliseconds) { run() }
        }
    }

    fun run() {
        if (runnables.hasItems) {
            runnables.deleteLast()!!.invoke()
            postOnMain(miliseconds) { run() }
        } else isRunning = false
    }

}
