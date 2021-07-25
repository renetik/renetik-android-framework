package renetik.android.json.store

import android.content.Context
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.CSContext
import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString
import renetik.android.logging.CSLog.logInfo

class CSJsonStore(id: String) : CSContext(), CSStoreInterface, CSJsonMapInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)
    override val data = preferences.getString("json", "{}")!!.parseJsonMap() ?: mutableMapOf()

    init {
        logInfo(data)
    }

    override fun save(key: String, value: String?) {
        data[key] = value
        save()
    }

    private fun save() {
//        val sortedMap: MutableMap<String, String> = LinkedHashMap()
//        map.keys.sorted().forEach { sortedMap[it] = map[it]!! }
        val jsonString = data.toSortedMap().toJsonString(formatted = application.isDebugBuild)
        preferences.edit().putString("json", jsonString).apply()
    }

    override fun load(store: CSStoreInterface) {
        data.putAll(store.data)
        save()
    }

    override fun reload(store: CSStoreInterface) {
        data.clear()
        load(store)
    }

    override fun has(key: String): Boolean = data.contains(key)
    override fun getString(key: String): String? = data[key]?.toString()
    override fun asStringMap(): Map<String, *> = data
    fun clear() {
        data.clear()
    }
}