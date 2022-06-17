package renetik.android.event

import renetik.android.event.registration.CSRegistration

interface CSEventListener<T> : CSRegistration {
    fun onEvent(argument: T)
}