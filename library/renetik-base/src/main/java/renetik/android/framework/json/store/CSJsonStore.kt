package renetik.android.framework.json.store

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJsonString
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.runIf
import java.io.Closeable

abstract class CSJsonStore(private val isJsonPretty: Boolean = false)
    : CSJsonObject(), Closeable {

    override val data by lazy { load() }

    abstract fun loadJsonString(): String?

    abstract fun saveJsonString(json: String)

    protected fun load() = loadJsonString()?.parseJsonMap() ?: mutableMapOf()

    fun save() {
        if (isBulkSave) return
        val json = data
            .runIf(isJsonPretty) { it.toSortedMap() }
            .toJsonString(formatted = isJsonPretty)
        saveJsonString(json)
    }

    override fun set(key: String, value: String?) {
        super.set(key, value)
        save()
    }

    override fun set(key: String, value: Map<String, *>?) {
        super.set(key, value)
        save()
    }

    override fun set(key: String, value: Array<*>?) {
        super.set(key, value)
        save()
    }

    override fun set(key: String, value: List<*>?) {
        super.set(key, value)
        save()
    }

    override fun <T : CSJsonObject> set(key: String, value: T?) {
        super.set(key, value)
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

    override fun close() {
        super.close()
        save()
    }
}