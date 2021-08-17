package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyBase

interface CSStoreEventProperty<T> : CSEventProperty<T> {
    val isSet: Boolean
    fun load(store: CSStoreInterface): T
    fun save(store: CSStoreInterface, value: T)
    fun reload()
}

abstract class CSStoreEventPropertyBase<T>(
    var store: CSStoreInterface, val key: String, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply), CSStoreEventProperty<T> {

    override val isSet get() = store.has(key)

    override fun reload() {
        val newValue = load(store)
        if (_value == newValue) return
        eventBeforeChange.fire(_value)
        _value = newValue
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}

fun <T> CSStoreEventPropertyBase<T>.save(store: CSStoreInterface) = save(store, value)

fun <T> CSStoreEventPropertyBase<T>.save() = save(store, value)