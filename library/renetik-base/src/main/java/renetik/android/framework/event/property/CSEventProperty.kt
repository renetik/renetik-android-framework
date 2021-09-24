package renetik.android.framework.event.property

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.lang.CSProperty

interface CSEventProperty<T> : CSProperty<T> {
    fun onChanged(function: (before: T, after: T) -> Unit): CSEventRegistration
    fun onChange(function: (T) -> Unit): CSEventRegistration
    fun value(newValue: T, fire: Boolean = true): CSEventProperty<T>
    fun apply(): CSEventProperty<T>
}