package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyBase

class CSStoreBooleanEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSEventPropertyBase<Boolean>(onChange) {

    override var _value: Boolean
        get() = store.getBoolean(key, default)
        set(value) = store.save(key, value)

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getBoolean(key, default))
}

open class CSStoreNullableBooleanEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Boolean?,
    onChange: ((value: Boolean?) -> Unit)?)
    : CSEventPropertyBase<Boolean?>(onChange) {

    override var _value: Boolean?
        get() = store.getBoolean(key, default)
        set(value) = store.save(key, value)

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getBoolean(key, default))
}

class CSStoredBooleanEventProperty(
    private var store: CSStoreInterface, val key: String,
    onChange: ((value: Boolean) -> Unit)?)
    : CSEventPropertyBase<Boolean>(onChange) {

    override var _value: Boolean
        get() = store.getBoolean(key)!!
        set(value) = store.save(key, value)

    override fun value(newValue: Boolean, fire: Boolean) {
        val current = store.getBoolean(key)
        if (current != null) {
            if (current == newValue) return
            if (fire) eventBeforeChange.fire(current)
        }
        _value = newValue
        onValueChanged(newValue)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getBoolean(key)!!)
}