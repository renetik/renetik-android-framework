package renetik.android.core.java.util.concurrent

import androidx.annotation.WorkerThread
import renetik.android.core.lang.catchAllError
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS

inline fun ScheduledExecutorService.background(
    delay: Long = 0, @WorkerThread crossinline function: () -> Unit,
) = schedule({ catchAllError(function) }, delay, MILLISECONDS)!!

inline fun ScheduledExecutorService.backgroundNano(
    delay: Long = 0, @WorkerThread crossinline function: () -> Unit,
) = schedule({ catchAllError(function) }, delay, NANOSECONDS)!!

inline fun ScheduledExecutorService.backgroundEach(
    delay: Long = 0, period: Long, @WorkerThread crossinline function: () -> Unit,
) = scheduleAtFixedRate({ catchAllError(function) }, delay, period, MILLISECONDS)!!

inline fun ScheduledExecutorService.backgroundEachNano(
    delay: Long = 0, period: Long, @WorkerThread crossinline function: () -> Unit,
): ScheduledFuture<*> = scheduleAtFixedRate({ catchAllError(function) }, delay, period, NANOSECONDS)

inline fun ScheduledExecutorService.backgroundEach(
    period: Long, @WorkerThread crossinline function: () -> Unit,
) = backgroundEach(delay = period, period = period, function = function)

inline fun ScheduledExecutorService.backgroundEachNano(
    period: Long, @WorkerThread crossinline function: () -> Unit,
) = backgroundEachNano(delay = period, period = period, function = function)
