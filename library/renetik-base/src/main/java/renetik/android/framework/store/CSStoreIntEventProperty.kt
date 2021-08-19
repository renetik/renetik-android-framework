package renetik.android.framework.store

class CSStoreIntEventProperty(
    store: CSStoreInterface, key: String, default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSStoreEventPropertyBase2<Int>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface): Int? = store.getInt(key)
    override fun save(store: CSStoreInterface, value: Int) = store.save(key, value)
}