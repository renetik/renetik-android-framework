package renetik.android.framework.event.property

import renetik.android.framework.event.CSRegistration
import renetik.android.framework.lang.property.CSProperty

interface CSEventProperty<T> : CSProperty<T> {
    fun value(newValue: T, fire: Boolean = true)
    fun onChange(function: (T) -> Unit): CSRegistration
}