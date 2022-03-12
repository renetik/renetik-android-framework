package renetik.android.framework.event

fun CSEventOwner.register(registration: CSEventRegistration) =
    registration.also { eventRegistrations.add(it) }

fun CSEventOwner.register(key: Any, registration: CSEventRegistration) =
    registration.also { eventRegistrations.add(key, it) }

fun CSEventOwner.cancel(registration: CSEventRegistration) =
    registration.also { eventRegistrations.cancel(it) }

fun CSEventOwner.remove(registration: CSEventRegistration) =
    registration.also { eventRegistrations.remove(it) }

@JvmName("CSEventOwnerRegisterNullable")
fun CSEventOwner.register(registration: CSEventRegistration?) =
    registration?.let { eventRegistrations.add(it) }

@JvmName("CSEventOwnerCancelNullable")
fun CSEventOwner.cancel(registration: CSEventRegistration?) =
    registration?.let { eventRegistrations.cancel(it) }

fun CSEventOwner.cancel(vararg registrations: CSEventRegistration?) =
    registrations.forEach { cancel(it) }

@JvmName("CSEventOwnerRemoveNullable")
fun CSEventOwner.remove(registration: CSEventRegistration?) =
    registration?.also { eventRegistrations.remove(it) }