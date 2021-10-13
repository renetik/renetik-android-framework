package renetik.android.framework.store

import android.annotation.SuppressLint
import android.content.Context
import renetik.android.framework.CSContext
import renetik.android.framework.common.catchAllWarnReturnNull
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.extensions.createJsonObject
import renetik.android.framework.json.extensions.createJsonObjectList
import renetik.android.framework.json.parseJson
import renetik.android.framework.json.parseJsonList
import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJsonString
import kotlin.reflect.KClass

class CSPreferencesStore(id: String) : CSContext(), CSStoreInterface {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)

    override val data: Map<String, Any?> get() = preferences.all

    override val eventLoaded = event()

    @SuppressLint("CommitPrefEdits")
    override fun clear() = preferences.edit().clear().apply()

    override fun clear(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun has(key: String) = preferences.contains(key)

    override fun set(key: String, value: String?) = value?.let {
        val editor = preferences.edit()
        editor.putString(key, it)
        editor.apply()
    } ?: clear(key)

    override fun get(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    override fun set(key: String, value: Map<String, *>?) = set(key, value?.toJsonString())
    override fun getMap(key: String): Map<String, *>? = get(key)?.parseJson<Map<String, *>>()

    // TODO Array can be retrieved and save by using list functions in extensions
    override fun set(key: String, value: Array<*>?) = set(key, value?.toJsonString())
    override fun getArray(key: String): Array<*>? = get(key)?.parseJson<List<*>>()?.toTypedArray()

    override fun set(key: String, value: List<*>?) = set(key, value?.toJsonString())
    override fun getList(key: String): List<*>? = get(key)?.parseJson<List<*>>()

    @Suppress("unchecked_cast")
    override fun <T : CSJsonObject> getJsonList(key: String, type: KClass<T>) =
        (get(key)?.parseJsonList() as? List<MutableMap<String, Any?>>)
            ?.let { type.createJsonObjectList(it) }

    override fun <T : CSJsonObject> set(key: String, jsonObject: T?) =
        set(key, jsonObject?.toJsonString())

    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>) =
        get(key)?.parseJsonMap()?.let { type.createJsonObject(it) }

    override fun load(store: CSStoreInterface) = with(preferences.edit()) {
        loadAll(store)
        eventLoaded.fire()
        apply()
    }

    override fun reload(store: CSStoreInterface) = with(preferences.edit()) {
        clear()
        loadAll(store)
        eventLoaded.fire()
        apply()
    }
}

