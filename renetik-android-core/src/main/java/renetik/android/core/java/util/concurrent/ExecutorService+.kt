package renetik.android.core.java.util.concurrent

import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.lang.result.invoke
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun ExecutorService.shutdownAndWait(second: Int = 5) {
    shutdown()
    awaitTermination(second.toLong(), TimeUnit.SECONDS)
}

suspend fun ExecutorService.shutdownWaiting(
    timeout: Duration = 5.seconds
): Boolean = app.IO {
    shutdown()
    awaitTermination(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
}