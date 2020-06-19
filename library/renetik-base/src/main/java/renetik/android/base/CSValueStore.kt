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

    fun getBoolean(key: String, defaultValue: Boolean = false) =
            getString(key)?.toBoolean() ?: defaultValue

    fun getDouble(key: String, defaultValue: Double = 0.0) =
            getString(key)?.asDouble(defaultValue) ?: defaultValue

    fun getLong(key: String, defaultValue: Long = 0L) =
            getString(key)?.asLong(defaultValue) ?: defaultValue

    fun getFloat(key: String, defaultValue: Float = 0F) =
            getString(key)?.asFloat(defaultValue) ?: defaultValue

    fun getInt(key: String, defaultValue: Int = 0) =
            getString(key)?.asInt(defaultValue) ?: defaultValue

    fun getString(key: String, defaultValue: String) = getString(key) ?: defaultValue

    fun getString(key: String): String? = tryAndWarn { preferences.getString(key, null) }

}
