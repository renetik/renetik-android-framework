package renetik.android.json.store

import android.content.Context
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.CSContext
import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString

class CSJsonStore(id: String) : CSContext(), CSStoreInterface, CSJsonMapInterface {
    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)
    private val data = preferences.getString("json", "{}")!!.parseJsonMap() ?: mutableMapOf()

    override fun save(key: String, value: String?) {
        data[key] = value
        val jsonString = data.toJsonString(formatted = application.isDebugBuild)
        preferences.edit().putString("json", jsonString).apply()
    }

    override fun has(key: String): Boolean = data.contains(key)
    override fun getString(key: String): String? = data[key]?.toString()
    override fun asStringMap(): Map<String, *> = data
}