package renetik.android.event.change

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.property.CSSafeHasChangeValue

fun <T> CSHasDestruct.safe(property: CSHasChangeValue<T>)
        : CSSafeHasChangeValue<T> = property.safeStateDelegate(this)