package renetik.android.event

import renetik.android.event.owner.CSEventOwner
import renetik.android.event.registration.CSRegistration

fun CSEventOwner.register(registration: CSRegistration) =
    registrations.add(registration)

fun CSEventOwner.register(key: Any, registration: CSRegistration): CSRegistration {
    registrations.add(key, registration)
    return registration
}

fun CSEventOwner.cancel(registration: CSRegistration) =
    registration.also { registrations.cancel(it) }

fun CSEventOwner.remove(registration: CSRegistration) =
    registration.also { registrations.remove(it) }

@JvmName("CSEventOwnerRegisterNullable")
fun CSEventOwner.register(registration: CSRegistration?) =
    registration?.let { registrations.add(it) }

@JvmName("CSEventOwnerCancelNullable")
fun CSEventOwner.cancel(registration: CSRegistration?) =
    registration?.let { registrations.cancel(it) }

fun CSEventOwner.cancel(vararg registrations: CSRegistration?) =
    registrations.forEach { cancel(it) }

fun CSEventOwner.cancel(registrations: List<CSRegistration>?) {
    registrations?.forEach { cancel(it) }
}

@JvmName("CSEventOwnerRemoveNullable")
fun CSEventOwner.remove(registration: CSRegistration?) =
    registration?.also { registrations.remove(it) }