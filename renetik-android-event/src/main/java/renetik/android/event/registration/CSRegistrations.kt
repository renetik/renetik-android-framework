package renetik.android.event.registration

import androidx.annotation.AnyThread

interface CSRegistrations : CSRegistration {

    @AnyThread
    fun register(registration: CSRegistration): CSRegistration

    @AnyThread
    fun register(replace: CSRegistration?, registration: CSRegistration?): CSRegistration?

    @AnyThread
    fun register(key: String, registration: CSRegistration?): CSRegistration?
}