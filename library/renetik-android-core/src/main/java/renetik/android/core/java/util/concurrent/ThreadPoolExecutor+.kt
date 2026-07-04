package renetik.android.core.java.util.concurrent

import java.util.concurrent.ThreadPoolExecutor

val ThreadPoolExecutor.debugString
    get() = "completedTaskCount:${completedTaskCount}\n" +
            "taskCount:${taskCount}\n" +
            "corePoolSize:${corePoolSize}\n" +
            "poolSize:${poolSize}\n" +
            "activeCount:${activeCount}"