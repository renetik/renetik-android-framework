package renetik.android.framework.store

import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
fun SharedPreferences.Editor.loadAll(store: CSStore) {
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