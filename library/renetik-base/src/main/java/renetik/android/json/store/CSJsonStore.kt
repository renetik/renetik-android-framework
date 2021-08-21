package renetik.android.json.store

import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.runIf
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString

abstract class CSJsonStore(private val isJsonPretty: Boolean = false)
    : CSStoreInterface, CSJsonMapInterface {

    override val data by lazy { load() }

    abstract fun loadJsonString(): String?

    abstract fun saveJsonString(json: String)

    private fun load() = loadJsonString()?.parseJsonMap() ?: mutableMapOf()

    protected fun save() {
        val json = data
            .runIf(isJsonPretty) { it.toSortedMap() }
            .toJsonString(formatted = isJsonPretty)
        saveJsonString(json)
    }

    override fun get(key: String): String? = data[key]?.toString()

    override fun save(key: String, value: String?) {
        data[key] = value
        save()
    }

    override fun has(key: String): Boolean = data.contains(key)

    override fun load(store: CSStoreInterface) {
        data.putAll(store.data)
        save()
    }

    override fun reload(store: CSStoreInterface) {
        data.clear()
        load(store)
    }

    override fun asStringMap(): Map<String, *> = data

    override fun clear() = data.clear()

    override fun clear(key: String) {
        data.remove(key)
        save()
    }
}