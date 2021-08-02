package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyImpl

class CSStoreDoubleEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSEventPropertyImpl<Double>(store.getDouble(key, default), onChange) {

    override fun value(newValue: Double, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getDouble(key, default))
}