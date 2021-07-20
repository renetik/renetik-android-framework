package renetik.android.framework

import android.content.Context
import renetik.android.extensions.isEqualTo
import renetik.android.extensions.load
import renetik.android.extensions.reload
import renetik.android.framework.common.catchAllWarnReturnNull
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong

interface CSValueStoreInterface {

    fun save(key: String, value: Int?)

    fun save(key: String, value: Boolean?)

    fun save(key: String, value: Float?)

    fun save(key: String, value: Double?)

    fun save(key: String, value: Long?)

    fun save(key: String, value: String?)

    fun has(key: String): Boolean

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun getDouble(key: String, defaultValue: Double = 0.0): Double

    fun getDouble(key: String, defaultValue: Double? = null): Double?

    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun getFloat(key: String, defaultValue: Float = 0F): Float

    fun getFloat(key: String, defaultValue: Float? = null): Float?

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun getString(key: String, defaultValue: String): String

    fun getString(key: String): String?
}

class CSValueStore(id: String) : CSContextController(), CSValueStoreInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)

    override fun equals(other: Any?): Boolean {
        return super<CSContextController>.equals(other)
    }

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

    override fun save(key: String, value: Double?) = save(key, value?.toString())

    override fun save(key: String, value: Long?) = save(key, value?.toString())

    override fun has(key: String) = preferences.contains(key)

    override fun getBoolean(key: String, defaultValue: Boolean) =
        getString(key)?.toBoolean() ?: defaultValue

    override fun getDouble(key: String, defaultValue: Double) =
        getString(key)?.asDouble(defaultValue) ?: defaultValue

    override fun getDouble(key: String, defaultValue: Double?) =
        getString(key)?.asDouble() ?: defaultValue

    override fun getLong(key: String, defaultValue: Long) =
        getString(key)?.asLong(defaultValue) ?: defaultValue

    override fun getFloat(key: String, defaultValue: Float) =
        getString(key)?.asFloat(defaultValue) ?: defaultValue

    override fun getFloat(key: String, defaultValue: Float?) =
        getString(key)?.asFloat() ?: defaultValue

    override fun getInt(key: String, defaultValue: Int) =
        getString(key)?.asInt(defaultValue) ?: defaultValue

    override fun getString(key: String, defaultValue: String) = getString(key) ?: defaultValue

    override fun getString(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    fun clone(id: String) = CSValueStore(id).also { it.preferences.reload(preferences) }

    fun load(store: CSValueStore) = apply { preferences.load(store.preferences) }

    fun reload(store: CSValueStore) = apply { preferences.reload(store.preferences) }
    fun isEqualTo(otherStore: CSValueStore) = preferences.isEqualTo(otherStore.preferences)
}

fun <T> CSValueStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedValueHashCode = getInt(key)
    val value = values.find { it.hashCode() == savedValueHashCode }
    return value ?: default
}
