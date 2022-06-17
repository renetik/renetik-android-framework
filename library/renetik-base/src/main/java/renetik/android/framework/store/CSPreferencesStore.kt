package renetik.android.framework.store

import android.annotation.SuppressLint
import android.content.Context
import renetik.android.core.extensions.content.isDebug
import renetik.android.core.lang.catchAllWarnReturnNull
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.owner.CSContext
import renetik.android.json.*
import kotlin.reflect.KClass

class CSPreferencesStore(id: String) : CSContext(), CSStore {

    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)
    override val eventChanged = event<CSStore>()
    override val data: Map<String, Any?> get() = preferences.all

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
        eventChanged.fire(this@CSPreferencesStore)
        editor.apply()
    } ?: clear(key)

    override fun get(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    override fun set(key: String, value: Map<String, *>?) =
        set(key, value?.toJsonString(formatted = isDebug))

    override fun getMap(key: String): Map<String, *>? =
        get(key)?.parseJson<Map<String, *>>()

    // TODO Array can be retrieved and save by using list functions in extensions
    override fun set(key: String, value: Array<*>?) =
        set(key, value?.toJsonString(formatted = isDebug))

    override fun getArray(key: String): Array<*>? =
        get(key)?.parseJson<List<*>>()?.toTypedArray()

    override fun set(key: String, value: List<*>?) =
        set(key, value?.toJsonString(formatted = isDebug))

    override fun getList(key: String): List<*>? =
        get(key)?.parseJson<List<*>>()

    @Suppress("unchecked_cast")
    override fun <T : renetik.android.json.CSJsonObject> getJsonObjectList(key: String,
                                                                           type: KClass<T>) =
        (get(key)?.parseJsonList() as? List<MutableMap<String, Any?>>)
            ?.let(type::createJsonObjectList)

    override fun <T : renetik.android.json.CSJsonObject> set(key: String, value: T?) =
        set(key, value?.toJsonString(formatted = isDebug))

    override fun <T : renetik.android.json.CSJsonObject> getJsonObject(key: String,
                                                                       type: KClass<T>) =
        get(key)?.parseJsonMap()?.let(type::createJsonObject)

    override fun load(store: CSStore) = with(preferences.edit()) {
        loadAll(store)
        eventChanged.fire(this@CSPreferencesStore)
        apply()
    }

    override fun reload(store: CSStore) = with(preferences.edit()) {
        clear()
        loadAll(store)
        eventChanged.fire(this@CSPreferencesStore)
        apply()
    }
}

