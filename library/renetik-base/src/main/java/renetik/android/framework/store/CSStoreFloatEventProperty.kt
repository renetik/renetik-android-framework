package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty

class CSStoreFloatEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSEventProperty<Float>(store.getFloat(key, default), onChange) {

    override fun value(newValue: Float, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getFloat(key, default))
}