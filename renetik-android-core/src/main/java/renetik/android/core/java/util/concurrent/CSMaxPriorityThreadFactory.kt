package renetik.android.core.java.util.concurrent

import java.lang.Thread.MAX_PRIORITY
import java.util.concurrent.ThreadFactory

class CSMaxPriorityThreadFactory : ThreadFactory {
    override fun newThread(runnable: Runnable): Thread {
        return Thread(runnable).apply { priority = MAX_PRIORITY }
    }
}
