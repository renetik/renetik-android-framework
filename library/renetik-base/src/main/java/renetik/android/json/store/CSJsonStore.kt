package renetik.android.json.store

import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.runIf
import renetik.android.json.CSJsonListInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString

abstract class CSJsonStore(private val isJsonPretty: Boolean = false)
    : CSJsonMapStore() {

    override val data by lazy { load() }

    abstract fun loadJsonString(): String?

    abstract fun saveJsonString(json: String)

    protected fun load() = loadJsonString()?.parseJsonMap() ?: mutableMapOf()

    protected fun save() {
        val json = data
            .runIf(isJsonPretty) { it.toSortedMap() }
            .toJsonString(formatted = isJsonPretty)
        saveJsonString(json)
    }

    override fun save(key: String, value: String?) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: Map<String, *>) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: Array<*>) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: List<*>) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: CSJsonMapInterface) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: CSJsonListInterface) {
        super.save(key, value)
        save()
    }

    override fun load(store: CSStoreInterface) {
        super.load(store)
        save()
    }

    override fun clear(key: String) {
        super.clear(key)
        save()
    }
}