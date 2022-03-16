package renetik.android.framework.event

fun CSVisibleEventOwner.whileVisible(registration: CSRegistration?) =
    registration?.let { whileShowing(it) }