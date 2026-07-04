package renetik.android.event.change

import renetik.android.event.registration.CSRegistration

interface CSSuspendHasChange<Argument> {
    fun onChange(function: suspend (Argument) -> Unit): CSRegistration
}