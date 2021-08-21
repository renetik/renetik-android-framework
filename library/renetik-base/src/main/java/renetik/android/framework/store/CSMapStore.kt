package renetik.android.framework.store

class CSMapStore : CSStoreInterface {
    private val _data = mutableMapOf<String, Any?>()
    override val data: Map<String, Any?> = _data
    override fun save(key: String, value: String?) = _data.set(key, value)
    override fun load(store: CSStoreInterface) = _data.putAll(store.data)
    override fun clear(key: String) {
        _data.remove(key)
    }

    override fun clear() = _data.clear()
}