package renetik.android.framework.protocol

import renetik.android.event.CSRegistration

fun CSVisibleEventOwner.whileVisible(registration: CSRegistration?) =
    registration?.let { whileShowing(it) }