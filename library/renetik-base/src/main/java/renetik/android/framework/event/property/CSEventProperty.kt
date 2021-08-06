package renetik.android.framework.event.property

import renetik.android.framework.event.CSEvent
import renetik.android.framework.lang.CSProperty

interface CSEventProperty<T> : CSProperty<T> {
    fun onBeforeChange(value: (T) -> Unit): CSEvent.CSEventRegistration
    fun onChange(value: (T) -> Unit): CSEvent.CSEventRegistration
    fun value(newValue: T, fire: Boolean = true)
    fun apply(): CSEventProperty<T>
}