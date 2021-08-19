package renetik.android.framework.store

class CSStoreBooleanEventProperty(
    store: CSStoreInterface, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSStoreEventPropertyBase2<Boolean>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getBoolean(key)
    override fun save(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}

//class CSStoreBooleanEventProperty(
//    store: CSStoreInterface, key: String, val default: Boolean,
//    onChange: ((value: Boolean) -> Unit)?)
//    : CSStoreEventPropertyBase<Boolean>(store, key, onChange) {
//    override var _value = load(store)
//    override fun load(store: CSStoreInterface) = store.getBoolean(key, default)
//    override fun save(store: CSStoreInterface, value: Boolean) = store.save(key, value)
//}

class CSStoreNullableBooleanEventProperty(
    store: CSStoreInterface, key: String, val default: Boolean?,
    onChange: ((value: Boolean?) -> Unit)?)
    : CSStoreEventPropertyBase<Boolean?>(store, key, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getBoolean(key, default)
    override fun save(store: CSStoreInterface, value: Boolean?) = store.save(key, value)
}

class CSStoredBooleanEventProperty(
    store: CSStoreInterface, key: String,
    onChange: ((value: Boolean) -> Unit)?)
    : CSStoreEventPropertyBase<Boolean>(store, key, onChange) {

    override var _value = load(store)

    override fun value(newValue: Boolean, fire: Boolean) {
        val current = store.getBoolean(key)
        if (current != null) {
            if (current == newValue) return
            if (fire) eventBeforeChange.fire(current)
        }
        _value = newValue
        save(store, newValue)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override fun load(store: CSStoreInterface) = store.getBoolean(key)!!
    override fun save(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}