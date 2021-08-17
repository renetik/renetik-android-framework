package renetik.android.framework.store

import renetik.android.framework.event.event
import renetik.android.framework.event.listen

class CSStoreIntEventProperty(
    store: CSStoreInterface, key: String, val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSStoreEventPropertyBase2<Int>(store, key, onChange) {

    override var _value = store.getInt(key) ?: run {
        save(store, default)
        default
    }

    override fun reload() {
        val newValue = store.getInt(key)
        if (newValue == null) {
            save(store, default)
            return
        }
        if (_value == newValue) return
        eventBeforeChange.fire(_value)
        _value = newValue
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun load(store: CSStoreInterface) = store.getInt(key, default)
    override fun save(store: CSStoreInterface, value: Int) = store.save(key, value)
}

abstract class CSStoreEventPropertyBase2<T>(
    var store: CSStoreInterface,
    val key: String,
    val onApply: ((value: T) -> Unit)? = null)
    : CSStoreEventProperty<T> {

    protected val eventBeforeChange = event<T>()
    protected val eventChange = event<T>()
    protected abstract var _value: T

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)

    override val isSet get() = store.has(key)

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override fun apply() = apply {
        val value = _value
        onApply?.invoke(value)
        eventChange.fire(value)
    }

    override fun toString() = value.toString()
}

//abstract class CSEventPropertyBase2<T>(
//    protected val onApply: ((value: T) -> Unit)? = null) : CSEventProperty<T> {
//    protected val eventBeforeChange = event<T>()
//    protected val eventChange = event<T>()
//    protected abstract var _value: T
//
//    override var value: T
//        get() = _value
//        set(value) = value(value)
//
//    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
//    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)
//
//    override fun apply() = apply {
//        val value = _value
//        onApply?.invoke(value)
//        eventChange.fire(value)
//    }
//
//    override fun toString() = value.toString()
//}