package renetik.android.json.store

import android.content.Context
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.CSContext
import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.runIf
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString
import renetik.android.logging.CSLog.logInfo

class CSJsonStore(id: String) : CSContext(), CSStoreInterface, CSJsonMapInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)
    override val data = load()

    init {
        logInfo(data)
    }

    private fun load() = preferences.getString("json", "{}")!!.parseJsonMap() ?: mutableMapOf()

    private fun save() {
        val json = data.runIf(application.isDebugBuild) { it.toSortedMap() }
            .toJsonString(formatted = application.isDebugBuild)
        preferences.edit().putString("json", json).apply()
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
}