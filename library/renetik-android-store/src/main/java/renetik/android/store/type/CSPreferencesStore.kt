package renetik.android.store.type

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import renetik.android.core.lang.catchAllWarnReturnNull
import renetik.android.event.CSEvent.Companion.event
import renetik.android.json.CSJson.isJsonPretty
import renetik.android.json.createJsonObject
import renetik.android.json.createJsonObjectList
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.parseJson
import renetik.android.json.parseJsonList
import renetik.android.json.parseJsonMap
import renetik.android.json.toJson
import renetik.android.store.CSStore
import renetik.android.store.extensions.loadAll
import kotlin.reflect.KClass

class CSPreferencesStore(val preferences: SharedPreferences) : CSStore {
    constructor(context: Context, id: String = "default")
            : this(context.getSharedPreferences(id, MODE_PRIVATE))

    override val eventLoaded = event<CSStore>()
    override val eventChanged = event<CSStore>()
    override val data: Map<String, Any?> get() = preferences.all
    override val jsonMap: Map<String, *> get() = data

    @SuppressLint("CommitPrefEdits")
    override fun clear() = preferences.edit { clear() }

    override fun clear(key: String) = preferences.edit { remove(key) }

    override fun has(key: String) = key in preferences

    override fun getString(key: String): String? =
        catchAllWarnReturnNull { preferences.getString(key, null) }

    override fun getList(key: String): List<*>? =
        getString(key)?.parseJson<List<*>>()

    override fun getMap(key: String): Map<String, *>? =
        getString(key)?.parseJson<Map<String, *>>()

    override fun set(key: String, string: String?) = string?.let {
        preferences.edit {
            putString(key, it)
            eventChanged.fire(this@CSPreferencesStore)
        }
    } ?: clear(key)

    override fun set(key: String, value: List<*>?) =
        set(key, value?.toJson(formatted = isJsonPretty))

    override fun set(key: String, value: Map<String, *>?) =
        set(key, value?.toJson(formatted = isJsonPretty))

    @Suppress("unchecked_cast")
    override fun <T : CSJsonObject> getJsonObjectList(key: String, type: KClass<T>) =
        (getString(key)?.parseJsonList() as? List<MutableMap<String, Any?>>)
            ?.let(type::createJsonObjectList)

    override fun <T : CSJsonObject> setJsonObjectList(key: String, list: List<T>?) =
        set(key, list?.toJson(formatted = isJsonPretty))

    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>) =
        getString(key)?.parseJsonMap()?.let(type::createJsonObject)

    override fun <T : CSJsonObject> setJsonObject(key: String, value: T?) =
        set(key, value?.toJson(formatted = isJsonPretty))

    override fun load(data: Map<String, Any?>) = with(preferences.edit()) { load(data) }

    override fun reload(data: Map<String, Any?>) = with(preferences.edit()) {
        clear()
        load(data)
    }

    private fun SharedPreferences.Editor.load(data: Map<String, Any?>) {
        loadAll(data)
        eventLoaded.fire(this@CSPreferencesStore)
        eventChanged.fire(this@CSPreferencesStore)
        apply()
    }
}

