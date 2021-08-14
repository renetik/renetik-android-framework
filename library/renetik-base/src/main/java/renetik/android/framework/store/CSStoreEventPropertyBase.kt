package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyBase

abstract class CSStoreEventPropertyBase<T>(
    var store: CSStoreInterface, val key: String, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() {
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

    abstract fun load(store: CSStoreInterface): T

    abstract fun save(store: CSStoreInterface, value: T)
}

fun <T> CSStoreEventPropertyBase<T>.save(store: CSStoreInterface) = save(store, value)