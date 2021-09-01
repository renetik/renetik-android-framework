package renetik.android.framework.store.property

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface

interface CSPresetStoreEventProperty<T> : CSStoreEventProperty<T> {
    fun load(store: CSStoreInterface): T
    fun save(store: CSStoreInterface, value: T)
    fun reload()

    override fun load(): T = load(store)
    override fun save(value: T) = save(store, value)
    fun save(store: CSStoreInterface) = save(store, value)
}

abstract class CSPresetStoreEventPropertyBase<T>(
    override var store: CSStoreInterface,
    override val key: String, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply), CSPresetStoreEventProperty<T> {

    override fun reload() {
        val newValue = load(store)
        if (_value == newValue) return
        eventBeforeChange.fire(_value)
        _value = newValue
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun value(newValue: T, fire: Boolean) = apply {
        if (_value == newValue) return this
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}