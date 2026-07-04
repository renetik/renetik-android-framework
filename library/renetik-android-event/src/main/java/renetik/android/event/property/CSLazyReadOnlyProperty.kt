package renetik.android.event.property

import renetik.android.core.lang.value.CSValue
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CSLazyReadOnlyProperty<T>(
    create: () -> CSValue<T>
) : ReadOnlyProperty<Any?, T> {
    companion object {
        fun <T> lazyValue(create: () -> CSValue<T>) = CSLazyReadOnlyProperty(create)
    }

    val property: CSValue<T> by lazy { create() }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        synchronized(this) { this.property.value }
}