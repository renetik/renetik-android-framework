package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreBooleanEventProperty(
    store: CSStoreInterface, val key: String, val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSStoreEventPropertyBase<Boolean>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getBoolean(key, default)
    override fun setValue(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}

class CSStoreNullableBooleanEventProperty(
    store: CSStoreInterface, val key: String, val default: Boolean?,
    onChange: ((value: Boolean?) -> Unit)?)
    : CSStoreEventPropertyBase<Boolean?>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getBoolean(key, default)
    override fun setValue(store: CSStoreInterface, value: Boolean?) = store.save(key, value)
}

class CSStoredBooleanEventProperty(
    store: CSStoreInterface, val key: String,
    onChange: ((value: Boolean) -> Unit)?)
    : CSStoreEventPropertyBase<Boolean>(store, onChange) {
    override var _value: Boolean
        get() = getValue(store)
        set(value) = setValue(store, value)

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

    override fun getValue(store: CSStoreInterface) = store.getBoolean(key)!!
    override fun setValue(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}