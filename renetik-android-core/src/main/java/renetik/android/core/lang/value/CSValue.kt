package renetik.android.core.lang.value

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface CSValue<T> : ReadOnlyProperty<Any?, T> {
    companion object {
        fun <T> value(value: T) = object : CSSafeValue<T> {
            override val value: T = value
        }

        inline fun <T> value(crossinline function: () -> T): CSValue<T> =
            object : CSValue<T> {
                override val value: T get() = function()
            }
    }

    val value: T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
}