package renetik.android.event.property

import renetik.android.core.lang.value.CSSafeValue
import renetik.android.event.change.CSHasChangeValue

interface CSSafeHasChangeValue<T> : CSSafeValue<T>, CSSafeHasChange<T>, CSHasChangeValue<T>