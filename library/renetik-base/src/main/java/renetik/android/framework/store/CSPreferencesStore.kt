package renetik.android.framework.store

import android.content.Context
import android.content.SharedPreferences
import renetik.android.framework.CSContext
import renetik.android.framework.common.catchAllWarnReturnNull

class CSPreferencesStore(id: String) : CSContext(), CSStoreInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)

    override val data: Map<String, Any?> get() = preferences.all

    override fun clear() = preferences.edit().clear().apply()

    fun clear(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun save(key: String, value: String?) = value?.let {
        val editor = preferences.edit()
        editor.putString(key, it)
        editor.apply()
    } ?: clear(key)

    override fun get(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    override fun has(key: String) = preferences.contains(key)

    override fun load(store: CSStoreInterface): Unit = with(preferences.edit()) {
        loadAll(store)
        apply()
    }

    override fun reload(store: CSStoreInterface) = with(preferences.edit()) {
        clear()
        loadAll(store)
        apply()
    }
}

@Suppress("UNCHECKED_CAST")
private fun SharedPreferences.Editor.loadAll(store: CSStoreInterface) {
    for (entry in store.data) {
        val value = entry.value ?: continue
        when (value) {
            is String -> putString(entry.key, value)
            is Set<*> -> putStringSet(entry.key, value as Set<String>)
            is Int -> putInt(entry.key, value)
            is Long -> putLong(entry.key, value)
            is Float -> putFloat(entry.key, value)
            is Boolean -> putBoolean(entry.key, value)
            else -> error("Unknown value type: $value")
        }
    }
}


