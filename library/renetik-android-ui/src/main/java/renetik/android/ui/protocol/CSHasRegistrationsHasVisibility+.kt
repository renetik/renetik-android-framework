package renetik.android.ui.protocol

import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.register

fun <T> T.registerUntilHide(registration: CSRegistration): CSRegistration
        where T : CSHasRegistrations, T : CSVisibility {
    register(registration)
    return untilHide(registration)
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