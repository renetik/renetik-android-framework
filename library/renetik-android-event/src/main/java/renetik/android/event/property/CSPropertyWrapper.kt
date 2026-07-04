package renetik.android.event.property

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel

abstract class CSPropertyWrapper<T>(
    parent: CSHasDestruct? = null) : CSModel(parent), CSProperty<T> {
    protected abstract val property: CSProperty<T>
    override fun value(newValue: T, fire: Boolean) = property.value(newValue, fire)
    override fun onChange(function: (T) -> Unit) = property.onChange(function)
    override var value: T
        get() = property.value
        set(value) = value(value)

    override fun fireChange() = property.fireChange()
}

abstract class CSSuspendPropertyWrapper<T>(
    parent: CSHasDestruct? = null) : CSModel(parent), CSSuspendProperty<T> {
    protected abstract val property: CSSuspendProperty<T>
    override suspend fun value(newValue: T, fire: Boolean) = property.value(newValue, fire)
    override fun onChange(function: suspend (T) -> Unit) = property.onChange(function)
    override val value: T get() = property.value
    override suspend fun fireChange() = property.fireChange()
}