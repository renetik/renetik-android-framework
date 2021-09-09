package renetik.android.json.data

import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.CSJsonListInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.toJsonString

//TODO rename to CSMapStore
@Suppress("unchecked_cast")
open class CSJsonMapStore() : Iterable<Map.Entry<String, Any?>>,
    CSJsonMapInterface, CSStoreInterface {

    constructor(map: MutableMap<String, Any?>) : this() {
        load(map)
    }

    override val data = mutableMapOf<String, Any?>()

    var index: Int? = null
    var key: String? = null
    var isLoaded = false

    fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        if (!isLoaded) {
            isLoaded = true
            onLoadedFirstTime()
        }
    }

    protected open fun onLoadedFirstTime() {
    }

    override fun toString() = toJsonString(formatted = true)

    override fun save(key: String, value: String?) = data.set(key, value)
    override fun save(key: String, value: Map<String, *>) = data.set(key, value)
    override fun save(key: String, value: Array<*>) = data.set(key, value)
    override fun save(key: String, value: List<*>) = data.set(key, value)
    override fun save(key: String, value: CSJsonMapInterface) = data.set(key, value)
    override fun save(key: String, value: CSJsonListInterface) = data.set(key, value)

    override fun load(store: CSStoreInterface) = load(store.data)
    override fun clear() = data.clear()
    override fun clear(key: String) {
        data.remove(key)
    }
}



