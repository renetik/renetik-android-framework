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
        onHidingRegistration.cancel()
        cancel(registration)
    })
}

fun <T> T.registerUntilShow(registration: CSRegistration?)
        where T : CSHasRegistrations, T : CSVisibility {
    if (registration == null) return
    register(registration)
    register(onShowing { onShowingRegistration ->
        onShowingRegistration.cancel()
        cancel(registration)
    })
}

//fun <T> T.registerActiveIfVisible(registration: CSRegistration): CSRegistration
//        where T : CSHasRegistrations, T : CSVisibility {
//    register(registration)
//    val onVisibilityRegistration = onVisibility(registration::setActive)
//    return CSRegistration(
//        onResume = { registration.resume() },
//        onPause = { registration.pause() },
//        onCancel = {
//            cancel(registration)
//            onVisibilityRegistration.cancel()
//        })
//}