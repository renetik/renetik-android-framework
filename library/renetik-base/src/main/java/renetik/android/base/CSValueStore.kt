package renetik.android.base

import android.content.Context
import android.content.SharedPreferences
import renetik.android.java.common.tryAndWarn
import renetik.android.java.extensions.primitives.asDouble
import renetik.android.java.extensions.primitives.asFloat
import renetik.android.java.extensions.primitives.asInt
import renetik.android.java.extensions.primitives.asLong

interface CSValueStoreInterface {

    fun save(key: String, value: Int?)

    fun save(key: String, value: Boolean?)

    fun save(key: String, value: Float?)

    fun save(key: String, value: Long?)

    fun save(key: String, value: String?)

    fun has(key: String): Boolean

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun getDouble(key: String, defaultValue: Double = 0.0): Double

    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun getFloat(key: String, defaultValue: Float = 0F): Float

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun getString(key: String, defaultValue: String): String

    fun getString(key: String): String?
}

class CSValueStore(name: String) : CSContextController(), CSValueStoreInterface {

    val preferences: SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)

    fun clear() = preferences.edit().clear().apply()

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

    override fun save(key: String, value: Int?) = save(key, value?.toString())

    override fun save(key: String, value: Boolean?) = save(key, value?.toString())

    override fun save(key: String, value: Float?) = save(key, value?.toString())

    override fun save(key: String, value: Long?) = save(key, value?.toString())

    override fun has(key: String) = preferences.contains(key)

    override fun getBoolean(key: String, defaultValue: Boolean) =
        getString(key)?.toBoolean() ?: defaultValue

    override fun getDouble(key: String, defaultValue: Double) =
        getString(key)?.asDouble(defaultValue) ?: defaultValue

    override fun getLong(key: String, defaultValue: Long) =
        getString(key)?.asLong(defaultValue) ?: defaultValue

    override fun getFloat(key: String, defaultValue: Float) =
        getString(key)?.asFloat(defaultValue) ?: defaultValue

    override fun getInt(key: String, defaultValue: Int) =
        getString(key)?.asInt(defaultValue) ?: defaultValue

    override fun getString(key: String, defaultValue: String) = getString(key) ?: defaultValue

    override fun getString(key: String): String? = tryAndWarn { preferences.getString(key, null) }

}
