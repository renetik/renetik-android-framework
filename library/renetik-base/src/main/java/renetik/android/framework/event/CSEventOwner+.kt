package renetik.android.framework.event

fun CSEventOwner.register(registration: CSEventRegistration?) =
    registration?.let { register(it) }