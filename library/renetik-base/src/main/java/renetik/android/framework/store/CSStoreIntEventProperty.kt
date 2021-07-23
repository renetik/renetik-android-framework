package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty

class CSStoreIntEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSEventProperty<Int>(store.getInt(key, default), onChange) {

    override fun value(newValue: Int, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getInt(key, default))
}