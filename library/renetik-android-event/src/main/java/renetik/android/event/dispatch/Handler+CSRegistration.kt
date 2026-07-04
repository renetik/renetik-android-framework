package renetik.android.event.dispatch

import android.os.Handler
import android.os.SystemClock.uptimeMillis
import renetik.android.core.lang.Fun
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.start
import kotlin.time.Duration

/**
 * LeakCanary was reporting false positives for leaks because removeCallbacks
 * looks like doesn't remove runnable immediately
 */
inline fun Handler.laterEach(
    after: Int, period: Int = after,
    start: Boolean = true, crossinline function: Fun,
): CSRegistration {
    val token = object {}
    lateinit var registration: CSRegistration
    lateinit var runnable: Fun
    runnable = {
        if (registration.isActive) {
            function()
            postAtTime(runnable, token, uptimeMillis() + period.toLong())
        }
    }
    registration = CSRegistration(
        onResume = { postAtTime(runnable, token, uptimeMillis() + after.toLong()) },
        onCancel = { removeCallbacksAndMessages(token) }
    )
    if (start) registration.start()
    return registration
}

inline fun Handler.laterEach(
    after: Int, period: Int = after, crossinline function: Fun,
) = laterEach(after, period, start = true, function)

inline fun Handler.later(after: Int, crossinline function: Fun): CSRegistration {
    val token = object {}
    var isCanceled = false
    postAtTime({ if (!isCanceled) function() }, token, uptimeMillis() + after.toLong())
    return CSRegistration(isActive = true) {
        isCanceled = true
        removeCallbacksAndMessages(token)
    }
}

inline fun Handler.later(crossinline function: Fun): CSRegistration {
    val token = object {}
    var isCanceled = false
    postAtTime({ if (!isCanceled) function() }, token, uptimeMillis())
    return CSRegistration(isActive = true) {
        isCanceled = true
        removeCallbacksAndMessages(token)
    }
}

inline fun Handler.laterEach(
    after: Duration, period: Duration = after,
    start: Boolean = true, crossinline function: Fun,
) = laterEach(after.inWholeMilliseconds.toInt(),
    period.inWholeMilliseconds.toInt(), start, function)

inline fun Handler.laterEach(
    after: Duration, period: Duration = after, crossinline function: Fun,
) = laterEach(after.inWholeMilliseconds.toInt(),
    period.inWholeMilliseconds.toInt(), start = true, function)

inline fun Handler.later(after: Duration, crossinline function: Fun) =
    later(after.inWholeMilliseconds.toInt(), function)