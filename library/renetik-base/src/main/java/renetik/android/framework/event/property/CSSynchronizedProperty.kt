package renetik.android.framework.event.property

import renetik.android.core.lang.CSSynchronizedValue
import renetik.android.core.lang.property.CSProperty

interface CSSynchronizedProperty<T> : CSSynchronizedValue<T>, CSProperty<T>

interface CSSynchronizedEventProperty<T> : CSSynchronizedProperty<T>, CSEventProperty<T>