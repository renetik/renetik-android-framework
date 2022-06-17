package renetik.android.core.kotlin.reflect

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

val KProperty0<*>.isLazyInitialized: Boolean
    get() {
        isAccessible = true
        return (getDelegate() as Lazy<*>).isInitialized()
    }

val <T> KProperty0<T>.orNull: T?
    get() = if (isLazyInitialized) get() else null