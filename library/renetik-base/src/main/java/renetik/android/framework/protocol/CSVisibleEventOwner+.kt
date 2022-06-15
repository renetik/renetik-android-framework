package renetik.android.framework.protocol

import renetik.android.framework.event.CSRegistration

fun CSVisibleEventOwner.whileVisible(registration: CSRegistration?) =
    registration?.let { whileShowing(it) }