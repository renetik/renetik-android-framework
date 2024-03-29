package renetik.android.ui.protocol

import renetik.android.core.lang.CSHandler.mainHandler
import renetik.android.core.lang.Func
import renetik.android.core.lang.value.isTrue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.laterEach
import renetik.android.event.registration.onTrue
import renetik.android.event.registration.plus
import renetik.android.event.registration.register
import renetik.android.event.registration.start
import kotlin.time.Duration

fun <T> T.registerUntilHide(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility {
    untilHide(this + registration)
    return registration
}

@JvmName("registerUntilHideRegistrationNullable")
fun <T> T.registerUntilHide(registration: CSRegistration?): CSRegistration?
        where T : CSHasRegistrations, T : CSVisibility =
    registration?.let { registerUntilHide(it) }

@JvmName("untilHideRegistrationNullable")
fun <T> T.untilHide(registration: CSRegistration?): CSRegistration?
        where T : CSHasRegistrations, T : CSVisibility = registration?.let(::untilHide)

fun <T> T.onShowUntilHide(registration: () -> CSRegistration)
        where T : CSHasRegistrations, T : CSVisibility {
    isVisible.onTrue { untilHide(registration()) }
}

fun <T> T.untilHide(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility =
    onHiding { onHidingRegistration ->
        onHidingRegistration.cancel()
        cancel(registration)
    }

fun <T> T.registerUntilShow(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility {
    this + registration
    register(onShowing { onShowingRegistration ->
        cancel(onShowingRegistration)
        cancel(registration)
    })
    return registration
}

@JvmName("registerUntilShowRegistrationNullable")
fun <T> T.registerUntilShow(registration: CSRegistration?): CSRegistration?
        where T : CSHasRegistrations, T : CSVisibility {
    if (registration == null) return null
    return registerUntilShow(registration)
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
                registration = registerUntilHide(mainHandler.laterEach(delay, period, function))
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