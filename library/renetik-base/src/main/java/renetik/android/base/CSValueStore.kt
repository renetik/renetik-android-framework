package renetik.android.base

import android.content.Context
import android.content.SharedPreferences
import renetik.android.java.extensions.primitives.asDouble
import renetik.android.java.extensions.primitives.asFloat
import renetik.android.java.extensions.primitives.asInt
import renetik.android.java.extensions.primitives.asLong
import renetik.android.java.common.tryAndWarn

class CSValueStore(name: String) : CSContextController() {

    val preferences: SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)

    fun clear() = preferences.edit().clear().apply()

    fun clear(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun save(key: String, value: String?) = value?.let {
        val editor = preferences.edit()
        editor.putString(key, it)
        editor.apply()
    } ?: clear(key)

    fun save(key: String, value: Int?) = save(key, value?.toString())

    fun save(key: String, value: Boolean?) = save(key, value?.toString())

    fun save(key: String, value: Float?) = save(key, value?.toString())

    fun save(key: String, value: Long?) = save(key, value?.toString())

    fun has(key: String) = preferences.contains(key)

    fun loadBoolean(key: String, defaultValue: Boolean = false) =
            loadString(key)?.toBoolean() ?: defaultValue

    fun loadDouble(key: String, defaultValue: Double = 0.0) =
            loadString(key)?.asDouble(defaultValue) ?: defaultValue

    fun loadLong(key: String, defaultValue: Long = 0L) =
            loadString(key)?.asLong(defaultValue) ?: defaultValue

    fun loadFloat(key: String, defaultValue: Float = 0F) =
            loadString(key)?.asFloat(defaultValue) ?: defaultValue

    fun loadInt(key: String, defaultValue: Int = 0) =
            loadString(key)?.asInt(defaultValue) ?: defaultValue

    fun loadString(key: String, defaultValue: String) = loadString(key) ?: defaultValue

    fun loadString(key: String): String? = tryAndWarn { preferences.getString(key, null) }

}
