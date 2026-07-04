package renetik.android.event.lifecycle

import androidx.annotation.AnyThread
import renetik.android.event.registration.CSRegistration

@Synchronized
@AnyThread
fun CSModel.register(key: String, registration: CSRegistration?): CSRegistration? =
    registrations.register(key, registration)