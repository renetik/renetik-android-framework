package renetik.android.java.event.property

import renetik.android.framework.lang.CSProperty
import renetik.android.java.event.CSEvent

interface CSEventPropertyInterface<T> : CSProperty<T> {
    fun onBeforeChange(value: (T) -> Unit): CSEvent.CSEventRegistration
    fun onChange(value: (T) -> Unit): CSEvent.CSEventRegistration
    fun value(newValue: T, fireEvents: Boolean = true)
}