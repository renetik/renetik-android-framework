package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreStringEventProperty(
    store: CSStoreInterface, val key: String, val default: String,
    onChange: ((value: String) -> Unit)?) : CSStoreEventPropertyBase<String>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getString(key, default)
    override fun setValue(store: CSStoreInterface, value: String) = store.save(key, value)
}

class CSStoredStringEventProperty(
    store: CSStoreInterface, val key: String, onChange: ((value: String) -> Unit)?)
    : CSStoreEventPropertyBase<String>(store, onChange) {
    override var _value: String
        get() = getValue(store)
        set(value) = setValue(store, value)

    override fun value(newValue: String, fire: Boolean) {
        val current = store.getString(key)
        if (current != null) {
            if (current == newValue) return
            if (fire) eventBeforeChange.fire(current)
        }
        _value = newValue
        onValueChanged(newValue)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override fun getValue(store: CSStoreInterface) = store.getString(key)!!
    override fun setValue(store: CSStoreInterface, value: String) = store.save(key, value)
}