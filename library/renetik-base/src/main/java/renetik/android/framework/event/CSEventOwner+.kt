package renetik.android.framework.event

fun CSEventOwner.register(registration: CSEventRegistration) =
    registration.also { eventRegistrations.add(it) }

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

@JvmName("CSEventOwnerRemoveNullable")
fun CSEventOwner.remove(registration: CSEventRegistration?) =
    registration?.also { eventRegistrations.remove(it) }

fun CSEventOwner.later(delayMilliseconds: Int = 0, function: () -> Unit): CSEventRegistration {
    lateinit var registration: CSEventRegistration
    registration = register(renetik.kotlin.later(delayMilliseconds) {
        function()
        remove(registration)
    })
    return registration
}

fun CSEventOwner.later(function: () -> Unit) = later(0, function)