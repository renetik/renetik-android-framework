package renetik.android.lang

import renetik.java.collections.list
import renetik.java.lang.CSLang

class CSRunConsolidator(private val miliseconds: Int) {

    private val runnables = list<() -> Unit>()
    private var isRunning = CSLang.NO

    operator fun invoke(runnable: () -> Unit) {
        if (isRunning) runnables.add(runnable)
        else {
            runnable()
            isRunning = CSLang.YES
            doLater(miliseconds) { run() }
        }
    }

    fun run() {
        if (runnables.hasItems) {
            runnables.deleteLast()!!.invoke()
            doLater(miliseconds) { run() }
        } else isRunning = CSLang.NO
    }

}
