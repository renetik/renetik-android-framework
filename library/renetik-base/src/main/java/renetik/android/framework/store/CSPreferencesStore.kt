package renetik.android.framework.store

import android.annotation.SuppressLint
import android.content.Context
import renetik.android.framework.CSContext
import renetik.android.framework.common.catchAllWarnReturnNull
import renetik.android.framework.json.CSJsonListInterface
import renetik.android.framework.json.CSJsonMapInterface
import renetik.android.framework.json.toJsonString

class CSPreferencesStore(id: String) : CSContext(), CSStoreInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)

    override val data: Map<String, Any?> get() = preferences.all

    @SuppressLint("CommitPrefEdits")
    override fun clear() = preferences.edit().clear().apply()

    override fun clear(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun save(key: String, value: String?) = value?.let {
        val editor = preferences.edit()
        editor.putString(key, it)
        editor.apply()
    } ?: clear(key)

    override fun save(key: String, value: Map<String, *>) = save(key, value.toJsonString())
    override fun save(key: String, value: Array<*>) = save(key, value.toJsonString())
    override fun save(key: String, value: List<*>) = save(key, value.toJsonString())
    override fun save(key: String, value: CSJsonMapInterface) = save(key, value.toJsonString())
    override fun save(key: String, value: CSJsonListInterface) = save(key, value.toJsonString())

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

