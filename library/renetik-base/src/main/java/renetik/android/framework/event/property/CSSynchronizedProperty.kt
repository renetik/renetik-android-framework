package renetik.android.framework.event.property

import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.property.CSProperty

interface CSSynchronizedValue<T> : CSValue<T>

interface CSSynchronizedProperty<T> : CSSynchronizedValue<T>, CSProperty<T>

interface CSSynchronizedEventProperty<T> : CSSynchronizedProperty<T>, CSEventProperty<T>