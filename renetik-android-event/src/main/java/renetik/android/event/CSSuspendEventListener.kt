package renetik.android.event

import renetik.android.event.registration.CSRegistration

interface CSSuspendEventListener<T> : CSRegistration {
    suspend operator fun invoke(argument: T)
}