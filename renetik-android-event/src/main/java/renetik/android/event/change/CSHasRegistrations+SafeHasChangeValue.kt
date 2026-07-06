package renetik.android.event.change

import renetik.android.core.lang.value.CSSafeValue
import renetik.android.event.registration.CSHasRegistrations

fun <T> CSHasRegistrations.safeValue(property: CSHasChangeValue<T>)
        : CSSafeValue<T> = property.safeValue(this)