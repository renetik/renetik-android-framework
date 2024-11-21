package renetik.android.ui.protocol

import renetik.android.core.lang.CSHandler.mainHandler
import renetik.android.core.lang.Func
import renetik.android.core.lang.value.isTrue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.actionFalse
import renetik.android.event.registration.actionTrue
import renetik.android.event.registration.laterEach
import renetik.android.event.registration.plus
import renetik.android.event.registration.start
import kotlin.time.Duration

@JvmName("untilHideRegistrationNullable")
fun <T> T.untilHide(registration: CSRegistration?): CSRegistration?
        where T : CSHasRegistrations, T : CSVisibility =
    registration?.let(::untilHide)

fun <T> T.onShowUntilHide(registration: () -> CSRegistration?)
        where T : CSHasRegistrations, T : CSVisibility {
    isVisible.actionTrue { untilHide(registration()) }
}

fun <T> T.untilHide(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility =
    onHiding { onHidingRegistration ->
        onHidingRegistration.cancel()
        registration.cancel()
    }

@JvmName("untilShowRegistrationNullable")
fun <T> T.untilShow(registration: CSRegistration?): CSRegistration?
        where T : CSHasRegistrations, T : CSVisibility = registration?.let(::untilShow)

fun <T> T.onHideUntilShow(registration: () -> CSRegistration)
        where T : CSHasRegistrations, T : CSVisibility {
    isVisible.actionFalse { untilShow(registration()) }
}

fun <T> T.untilShow(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility =
    onShowing { onShowingRegistration ->
        onShowingRegistration.cancel()
        registration.cancel()
    }

fun <T> T.laterEachIfShowing(period: Duration, function: Func): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility =
    laterEachIfShowing(period.inWholeMilliseconds.toInt(), function)

fun <T> T.laterEachIfShowing(period: Int, function: Func): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility =
    laterEachIfShowing(period, period, function)

fun <T> T.laterEachIfShowing(delay: Int, period: Int, function: Func): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility {
    var registration: CSRegistration? = null
    var onShowingRegistration: CSRegistration? = null
    return CSRegistration(
        isActive = false,
        onResume = {
            fun start() {
                registration = untilHide(this + mainHandler.laterEach(delay, period, function))
            }
            if (isVisible.isTrue) start()
            onShowingRegistration = onShowing { start() }
        },
        onPause = {
            onShowingRegistration?.cancel()
            registration?.cancel()
        }
    ).start()
}