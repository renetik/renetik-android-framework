package renetik.android.framework.store

//class CSStoreFloatEventProperty(
//    store: CSStoreInterface, key: String, val default: Float,
//    onChange: ((value: Float) -> Unit)?)
//    : CSStoreEventPropertyBase<Float>(store, key, onChange) {
//    override var _value = load(store)
//    override fun load(store: CSStoreInterface) = store.getFloat(key, default)
//    override fun save(store: CSStoreInterface, value: Float) = store.save(key, value)
//}

class CSStoreFloatEventProperty(
    store: CSStoreInterface, key: String, default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSStoreEventPropertyBase2<Float>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getFloat(key)
    override fun save(store: CSStoreInterface, value: Float) = store.save(key, value)
}