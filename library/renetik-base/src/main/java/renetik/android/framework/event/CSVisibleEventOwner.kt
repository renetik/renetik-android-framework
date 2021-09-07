package renetik.android.framework.event

interface CSVisibleEventOwner {
    fun whileVisible(registration: CSEventRegistration): CSEventRegistration
}