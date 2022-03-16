package renetik.android.framework.event

fun CSEventOwner.register(registration: CSRegistration) =
    registration.also { eventRegistrations.add(it) }

fun CSEventOwner.register(key: Any, registration: CSRegistration) {
    eventRegistrations.add(key, registration)
}

fun CSEventOwner.cancel(registration: CSRegistration) =
    registration.also { eventRegistrations.cancel(it) }

fun CSEventOwner.remove(registration: CSRegistration) =
    registration.also { eventRegistrations.remove(it) }

@JvmName("CSEventOwnerRegisterNullable")
fun CSEventOwner.register(registration: CSRegistration?) =
    registration?.let { eventRegistrations.add(it) }

@JvmName("CSEventOwnerCancelNullable")
fun CSEventOwner.cancel(registration: CSRegistration?) =
    registration?.let { eventRegistrations.cancel(it) }

fun CSEventOwner.cancel(vararg registrations: CSRegistration?) =
    registrations.forEach { cancel(it) }

@JvmName("CSEventOwnerRemoveNullable")
fun CSEventOwner.remove(registration: CSRegistration?) =
    registration?.also { eventRegistrations.remove(it) }