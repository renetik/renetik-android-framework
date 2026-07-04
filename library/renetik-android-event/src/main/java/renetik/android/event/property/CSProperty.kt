package renetik.android.event.property

import renetik.android.core.kotlin.notImplemented
import renetik.android.core.lang.tuples.to
import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.change.CSHasChange
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.change.onChange
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus
import renetik.android.event.registration.register

interface CSProperty<T> : CSVariable<T>, CSHasChange<T>, CSHasChangeValue<T> {
    fun value(newValue: T, fire: Boolean = true)
    fun fireChange()
    fun pause(): Unit = notImplemented()
    fun resume(fireChange: Boolean = true): Unit = notImplemented()

    companion object {
        fun <T> empty(value: T): CSProperty<T> = object : CSProperty<T> {
            override fun value(newValue: T, fire: Boolean) = Unit
            override fun fireChange() = Unit
            override var value: T = value
            override fun onChange(function: (T) -> Unit) = CSRegistration.Empty
        }

        val emptyBoolean: CSProperty<Boolean> = empty(false)
        val emptyString: CSProperty<String> = empty("")
        val emptyInt: CSProperty<Int> = empty(0)


        fun <T> property(
            value: T, onChange: ((value: T) -> Unit)? = null,
        ): CSProperty<T> =
            CSPropertyImpl(value, onChange)

        fun <T> property(
            onChange: ((value: T?) -> Unit)? = null,
        ): CSProperty<T?> =
            CSPropertyImpl(null, onChange)

        fun <T> lateProperty(
            onChange: ((value: T) -> Unit)? = null,
        ): CSLateProperty<T> =
            CSLateProperty(onChange)

        fun <T> propertyDelegate(
            parent: CSHasRegistrations,
            property: CSProperty<T>,
            onChange: ((value: T) -> Unit)? = null,
        ): CSProperty<T> = lateProperty(onChange)
            .apply { parent + connect(property) }

        fun <T> nullableProperty(
            onChange: ((value: T?) -> Unit)? = null,
        ): CSProperty<T?> = CSPropertyImpl(null, onChange)

        inline fun <Argument1, Argument2, Argument3>
                Pair<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>>.property(
            parent: CSHasRegistrations? = null,
            crossinline from: (Argument1, Argument2) -> Argument3,
        ): CSProperty<Argument3> {
            val property = property(from(first.value, second.value))
            (first to second).onChange { first, second ->
                property.value = from(first, second)
            }.also { parent?.register(it) }
            return property
        }

        inline fun <Argument1, Argument2, Argument3, Argument4> property(
            parent: CSHasRegistrations? = null,
            item1: CSHasChangeValue<Argument1>,
            item2: CSHasChangeValue<Argument2>,
            item3: CSHasChangeValue<Argument3>,
            crossinline from: (Argument1, Argument2, Argument3) -> Argument4,
        ): CSProperty<Argument4> {
            val property = property(from(item1.value, item2.value, item3.value))
            (item1 to item2 to item3).onChange { item1, item2, item3 ->
                property.value = from(item1, item2, item3)
            }.also { parent?.register(it) }
            return property
        }
    }
}