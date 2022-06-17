package renetik.android.framework.protocol

import renetik.android.event.registration.CSRegistration

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSRegistration): CSRegistration
}