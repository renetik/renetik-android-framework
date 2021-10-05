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

    override fun save(key: String, value: String?) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: Map<String, *>?) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: Array<*>?) {
        super.save(key, value)
        save()
    }

    override fun save(key: String, value: List<*>?) {
        super.save(key, value)
        save()
    }

    override fun <T : CSJsonObject> save(key: String, jsonObject: T?) {
        super.save(key, jsonObject)
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

    private var isBulkSave = false

    override fun bulkSave() = apply {
        isBulkSave = true
        return this
    }
    override fun close() {
        isBulkSave = false
        save()
    }
}