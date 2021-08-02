package renetik.android.json.data

import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.toJsonString

@Suppress("unchecked_cast")
open class CSJsonMap() : Iterable<Map.Entry<String, Any?>>, CSJsonMapInterface, CSStoreInterface {

    constructor(map: MutableMap<String, Any?>) : this() {
        load(map)
    }

    override val data = mutableMapOf<String, Any?>()

    var index: Int? = null
    var key: String? = null
    var isLoaded = false

    fun load(map: Map<String, Any?>) {
        data.putAll(map)
        if (!isLoaded) {
            isLoaded = true
            onLoadedFirstTime()
        }
    }

    protected open fun onLoadedFirstTime() {
    }

    override fun toString() = toJsonString(formatted = true)
    override fun asStringMap(): Map<String, *> = data

    override fun save(key: String, value: String?) = data.set(key, value)
    override fun load(store: CSStoreInterface) = load(store.data)
    override fun clear() = data.clear()
}



