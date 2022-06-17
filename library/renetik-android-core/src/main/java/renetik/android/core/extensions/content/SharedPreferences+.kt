package renetik.android.core.extensions.content

import android.content.SharedPreferences

fun SharedPreferences.copyTo(destination: SharedPreferences) = destination.load(this)

fun SharedPreferences.load(source: SharedPreferences) = with(edit()) {
    loadAll(source)
    apply()
}

fun SharedPreferences.reload(source: SharedPreferences) = with(edit()) {
    clear()
    loadAll(source)
    apply()
}

@Suppress("UNCHECKED_CAST")
private fun SharedPreferences.Editor.loadAll(source: SharedPreferences) {
    for (entry in source.all.entries) {
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