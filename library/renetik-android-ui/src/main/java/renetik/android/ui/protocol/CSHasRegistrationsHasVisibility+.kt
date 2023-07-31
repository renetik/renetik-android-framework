package renetik.android.ui.protocol

import renetik.android.core.lang.CSHandler.main
import renetik.android.core.lang.Func
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.laterEach
import renetik.android.event.registration.register
import renetik.android.event.registration.start

fun <T> T.registerUntilHide(registration: CSRegistration): CSRegistration
    where T : CSHasRegistrations, T : CSVisibility {
    register(registration)
    untilHide(registration)
    return registration
}

@JvmName("registerUntilHideRegistrationNullable")
fun <T> T.registerUntilHide(registration: CSRegistration?): CSRegistration?
    where T : CSHasRegistrations, T : CSVisibility =
    registration?.let { registerUntilHide(it) }

@JvmName("untilHideRegistrationNullable")
fun <T> T.untilHide(registration: CSRegistration?): CSRegistration?
    where T : CSHasRegistrations, T : CSVisibility = registration?.let {
    untilHide(it)
}

fun <T> T.untilHide(registration: CSRegistration): CSRegistration
    where T : CSHasRegistrations, T : CSVisibility =
    onHiding { onHidingRegistration ->
        onHidingRegistration.cancel()
        cancel(registration)
    }

fun <T> T.registerUntilShow(registration: CSRegistration): CSRegistration
    where T : CSHasRegistrations, T : CSVisibility {
    register(registration)
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

fun <T> T.laterEachIfShowing(period: Int, function: Func): CSRegistration
    where T : CSHasRegistrations, T : CSVisibility = laterEachIfShowing(period, period, function)

fun <T> T.laterEachIfShowing(delay: Int, period: Int, function: Func): CSRegistration
    where T : CSHasRegistrations, T : CSVisibility {
    var registration: CSRegistration? = null
    var onShowingRegistration: CSRegistration? = null
    return CSRegistration(
        isActive = true,
        onResume = {
            onShowingRegistration = onShowing {
                registration = registerUntilHide(main.laterEach(delay, period, function))
            }
        },
        onPause = {
            onShowingRegistration?.cancel()
            registration?.cancel()
        }
    ).start()
}