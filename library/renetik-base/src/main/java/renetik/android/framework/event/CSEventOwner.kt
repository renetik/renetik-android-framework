package renetik.android.framework.event

interface CSEventOwner {
    val eventRegistrations: CSEventRegistrations
    fun register(registration: CSEventRegistration) =
        registration.also { eventRegistrations.add(it) }
    fun cancel(registration: CSEventRegistration) =
        registration.also { eventRegistrations.cancel(it) }
}