package renetik.android.ui.protocol

import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.register

fun <T> T.registerUntilHide(registration: CSRegistration?)
        where T : CSHasRegistrations, T : CSVisibility {
    if (registration == null) return
    register(registration)
    register(onHiding { onHidingRegistration ->
        cancel(onHidingRegistration)
        cancel(registration)
    })
}

fun <T> T.registerUntilShow(registration: CSRegistration?)
        where T : CSHasRegistrations, T : CSVisibility {
    if (registration == null) return
    register(registration)
    register(onShowing { onShowingRegistration ->
        cancel(onShowingRegistration)
        cancel(registration)
    })
}