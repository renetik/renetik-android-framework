package renetik.android.framework.json.store

import renetik.android.framework.json.CSJsonObject
import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJsonString
import renetik.kotlin.runIf
import java.io.Closeable

abstract class CSJsonStore(private val isJsonPretty: Boolean = false)
    : CSJsonObject(), Closeable {

    override val data by lazy { load() }

    abstract fun loadJsonString(): String?

    abstract fun saveJsonString(json: String)

    protected fun load() = loadJsonString()?.parseJsonMap() ?: mutableMapOf()

    override fun onChanged() {
        super.onChanged()
        save()
    }

    fun save() {
        if (isBulkSave) return
        val json = data
            .runIf(isJsonPretty) { it.toSortedMap() }
            .toJsonString(formatted = isJsonPretty)
        saveJsonString(json)
    }
}