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

    override fun getString(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    override fun has(key: String) = preferences.contains(key)

    fun load(store: CSStore) = apply { preferences.load(store.preferences) }

    fun reload(store: CSStore) = apply { preferences.reload(store.preferences) }
}


