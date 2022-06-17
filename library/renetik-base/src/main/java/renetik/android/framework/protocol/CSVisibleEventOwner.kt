package renetik.android.framework.protocol

import renetik.android.event.CSRegistration

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSRegistration): CSRegistration
}