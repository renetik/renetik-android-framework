package renetik.android.event.dispatch

import androidx.annotation.WorkerThread
import renetik.android.core.java.util.concurrent.cancelInterrupt
import renetik.android.core.lang.catchAllError
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.NANOSECONDS
import kotlin.time.Duration

inline fun ScheduledExecutorService.background(
    delay: Duration? = null, @WorkerThread crossinline function: () -> Unit,
): CSRegistration = backgroundNano(delay?.inWholeNanoseconds ?: 0, function)

inline fun ScheduledExecutorService.backgroundNano(
    delay: Long, @WorkerThread crossinline function: () -> Unit,
): CSRegistration {
    val task = schedule({ catchAllError(function) }, delay, NANOSECONDS)!!
    return CSRegistration(onCancel = { task.cancelInterrupt() })
}

inline fun ScheduledExecutorService.backgroundEach(
    period: Duration, delay: Duration = period,
    @WorkerThread crossinline function: () -> Unit,
): CSRegistration = backgroundEachNano(
    delay.inWholeNanoseconds, period.inWholeNanoseconds, function
)

inline fun ScheduledExecutorService.backgroundEachNano(
    period: Long, delay: Long = period,
    @WorkerThread crossinline function: () -> Unit,
): CSRegistration {
    val task = scheduleAtFixedRate({ catchAllError(function) }, delay, period, NANOSECONDS)
    return CSRegistration(onCancel = { task.cancelInterrupt() })
}