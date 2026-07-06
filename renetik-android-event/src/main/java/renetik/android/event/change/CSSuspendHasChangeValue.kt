@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.event.change

import renetik.android.core.lang.value.CSValue

interface CSSuspendHasChangeValue<T> : CSValue<T>, CSSuspendHasChange<T>