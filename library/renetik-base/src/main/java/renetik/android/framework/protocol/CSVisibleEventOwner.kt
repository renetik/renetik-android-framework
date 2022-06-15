package renetik.android.framework.protocol

import renetik.android.framework.event.CSRegistration

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSRegistration): CSRegistration
}