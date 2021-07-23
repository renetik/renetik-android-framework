package renetik.android.framework.store

import android.content.Context
import renetik.android.extensions.load
import renetik.android.extensions.reload
import renetik.android.framework.CSContext
import renetik.android.framework.common.catchAllWarnReturnNull
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong

class CSStore(id: String) : CSContext(), CSStoreInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)

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

    fun clone(id: String) = CSStore(id).also { it.preferences.reload(preferences) }

    fun load(store: CSStore) = apply { preferences.load(store.preferences) }

    fun reload(store: CSStore) = apply { preferences.reload(store.preferences) }
}


