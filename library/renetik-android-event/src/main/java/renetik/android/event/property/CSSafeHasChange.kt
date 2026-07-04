package renetik.android.event.property

import renetik.android.event.change.CSHasChange
import renetik.android.event.registration.CSRegistration

interface CSSafeHasChange<T> : CSHasChange<T> {
    fun onUnsafeChange(function: (T) -> Unit): CSRegistration
}