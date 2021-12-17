package renetik.android.framework.event

fun CSVisibleEventOwner.whileVisible(registration: CSEventRegistration?) =
    registration?.let { whileShowing(it) }