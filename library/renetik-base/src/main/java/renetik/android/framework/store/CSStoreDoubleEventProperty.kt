package renetik.android.framework.store

//class CSStoreDoubleEventProperty(
//    store: CSStoreInterface, key: String, val default: Double,
//    onChange: ((value: Double) -> Unit)?)
//    : CSStoreEventPropertyBase<Double>(store, key, onChange) {
//    override var _value = load(store)
//    override fun load(store: CSStoreInterface) = store.getDouble(key, default)
//    override fun save(store: CSStoreInterface, value: Double) = store.save(key, value)
//}

class CSStoreDoubleEventProperty(
    store: CSStoreInterface, key: String, default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSStoreEventPropertyBase2<Double>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface): Double? = store.getDouble(key)
    override fun save(store: CSStoreInterface, value: Double) = store.save(key, value)
}