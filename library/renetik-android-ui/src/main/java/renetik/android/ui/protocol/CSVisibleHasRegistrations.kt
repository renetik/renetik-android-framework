package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrations

interface CSVisibleHasRegistrations {
    val visibilityRegistrations: CSRegistrations

    fun whileShowing(registration: CSRegistration) =
        registration.let { visibilityRegistrations.register(it) }

    fun whileShowingCancel(registration: CSRegistration) =
        registration.let { visibilityRegistrations.cancel(it) }
}