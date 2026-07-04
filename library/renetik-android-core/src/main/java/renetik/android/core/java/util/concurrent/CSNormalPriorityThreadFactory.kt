package renetik.android.core.java.util.concurrent

import java.util.concurrent.ThreadFactory

class CSNormalPriorityThreadFactory : ThreadFactory {
    override fun newThread(runnable: Runnable) = Thread {
//        catchAllWarn { setThreadPriority(-20) }
        runnable.run()
    }.also { it.priority = Thread.NORM_PRIORITY }
}