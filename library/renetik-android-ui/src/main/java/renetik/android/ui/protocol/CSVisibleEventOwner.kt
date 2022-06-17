package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSRegistration): CSRegistration
}