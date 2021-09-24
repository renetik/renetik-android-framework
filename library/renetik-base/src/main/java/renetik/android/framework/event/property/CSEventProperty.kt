package renetik.android.framework.event.property

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.lang.CSProperty

interface CSEventProperty<T> : CSProperty<T> {
    fun value(newValue: T, fire: Boolean = true)
    fun onChanged(function: (before: T, after: T) -> Unit): CSEventRegistration
    fun onChange(function: (T) -> Unit) = onChanged { _, after -> function(after) }
}