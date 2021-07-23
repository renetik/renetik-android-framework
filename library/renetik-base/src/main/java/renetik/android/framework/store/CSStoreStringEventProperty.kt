package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty

class CSStoreStringEventProperty(
    private var store: CSStoreInterface, val key: String, val default: String,
    onChange: ((value: String) -> Unit)?)
    : CSEventProperty<String>(store.getString(key, default), onChange) {

    override fun value(newValue: String, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue)
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
       reload()
    }

    fun reload() =  value(store.getString(key, default))
}