package renetik.android.core.lang

//@Deprecated("Unused remove ?")
//class CSRunConsolidator(private val miliseconds: Int) {
//
//    private val runnables = list<() -> Unit>()
//    private var isRunning = false
//
//    operator fun invoke(runnable: () -> Unit) {
//        if (isRunning) runnables.add(runnable)
//        else {
//            runnable()
//            isRunning = true
//            postOnMain(miliseconds) { run() }
//        }
//    }
//
//    fun run() {
//        if (runnables.hasItems) {
//            runnables.deleteLast()!!.invoke()
//            postOnMain(miliseconds) { run() }
//        } else isRunning = false
//    }
//
//}