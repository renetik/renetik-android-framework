package renetik.android.event.property

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause

object CSEventPropertyFunctions {
    fun <T> property(value: T, onApply: ((value: T) -> Unit)? = null) =
        CSEventPropertyImpl(value, onApply)

    fun <T> synchronizedProperty(value: T,
                                 onApply: ((value: T) -> Unit)? = null) =
        CSSynchronizedEventPropertyImpl(value, onApply)

    fun <T> synchronizedProperty(property: CSEventProperty<T>,
                                 onApply: ((value: T) -> Unit)? = null)
            : CSSynchronizedEventPropertyImpl<T> {
        val synchronized = CSSynchronizedEventPropertyImpl(property.value, onApply)
        lateinit var propertyOnChange: CSRegistration
        val synchronizedOnChange: CSRegistration = synchronized.onChange { value ->
            propertyOnChange.pause().use { property.value = value }
        }
        propertyOnChange = property.onChange { value ->
            synchronizedOnChange.pause().use { synchronized.value = value }
        }
        return synchronized
    }

    fun <T> lateProperty(onApply: ((value: T) -> Unit)? = null) =
        CSLateEventProperty(onApply)

    fun <T> nullableProperty(onApply: ((value: T?) -> Unit)? = null): CSEventProperty<T?> =
        CSEventPropertyImpl(null, onApply)
}