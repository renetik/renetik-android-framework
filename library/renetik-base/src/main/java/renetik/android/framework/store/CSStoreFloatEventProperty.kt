package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyImpl

class CSStoreFloatEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSEventPropertyImpl<Float>(store.getFloat(key, default), onChange) {

    override fun onValueChanged(newValue: Float) {
        super.onValueChanged(newValue)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getFloat(key, default))
}