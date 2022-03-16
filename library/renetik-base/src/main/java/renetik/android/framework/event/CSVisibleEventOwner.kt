package renetik.android.framework.event

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSRegistration): CSRegistration
}