package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty

class CSStoreBooleanEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSEventProperty<Boolean>(store.getBoolean(key, default), onChange) {

    override fun value(newValue: Boolean, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getBoolean(key, default))
}