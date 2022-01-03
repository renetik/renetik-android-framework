package renetik.android.framework.event

fun CSEventOwner.register(registration: CSEventRegistration) =
    registration.also { eventRegistrations.add(it) }

fun CSEventOwner.cancel(registration: CSEventRegistration) =
    registration.also { eventRegistrations.cancel(it) }

@JvmName("CSEventOwnerRegisterNullable")
fun CSEventOwner.register(registration: CSEventRegistration?) =
    registration?.let { eventRegistrations.add(it) }

@JvmName("CSEventOwnerCancelNullable")
fun CSEventOwner.cancel(registration: CSEventRegistration?) =
    registration?.let { eventRegistrations.cancel(it) }