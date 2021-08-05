package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyImpl

class CSStoreIntEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSEventPropertyImpl<Int>(store.getInt(key, default), onChange) {

    override fun onValueChanged(newValue: Int) {
        super.onValueChanged(newValue)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getInt(key, default))
}