package renetik.android.extensions

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

fun SharedPreferences.isEqualTo(other: SharedPreferences): Boolean {
    for (otherEntry in other.all.entries) {
        if (contains(otherEntry.key))
            if (otherEntry.value == null) return false
            else when (otherEntry.value) {
                is String -> if (getString(otherEntry.key, null) != otherEntry.value) return false
                is Set<*> -> if (getStringSet(otherEntry.key, null) != otherEntry.value) return false
                is Int -> if (getInt(otherEntry.key, 0) != otherEntry.value) return false
                is Long -> if (getLong(otherEntry.key, 0) != otherEntry.value) return false
                is Float -> if (getFloat(otherEntry.key, 0f) != otherEntry.value) return false
                is Boolean -> if (getBoolean(otherEntry.key, false) != otherEntry.value) return false
                else -> error("Unknown value type: ${otherEntry.value}")
            }
    }
    return true
}