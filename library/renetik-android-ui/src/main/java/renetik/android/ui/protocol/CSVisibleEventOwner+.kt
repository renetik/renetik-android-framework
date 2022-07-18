package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration

fun CSVisibleHasRegistrations.whileVisible(registration: CSRegistration?) =
    registration?.let { whileShowing(it) }