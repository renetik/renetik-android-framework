package renetik.android.framework.event

interface CSEventOwner {
    fun register(registration: CSEventRegistration): CSEventRegistration
}