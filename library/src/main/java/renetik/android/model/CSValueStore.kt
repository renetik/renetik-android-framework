package renetik.android.model

import android.content.Context
import renetik.android.extensions.primitives.asDouble
import renetik.android.extensions.primitives.asFloat
import renetik.android.extensions.primitives.asInt
import renetik.android.extensions.primitives.asLong
import renetik.android.java.collections.CSList
import renetik.android.java.collections.CSMap
import renetik.android.java.collections.list
import renetik.android.json.CSJsonData
import renetik.android.json.createList
import renetik.android.json.fromJson
import renetik.android.json.toJson
import renetik.android.lang.CSLang.empty
import renetik.android.lang.CSLang.no
import renetik.android.viewbase.CSContextController


@Suppress("unchecked_cast")
class CSValueStore(name: String) : CSContextController() {

    private val preferences = getSharedPreferences(name, Context.MODE_PRIVATE)

    fun clear() = preferences.edit().clear().apply()

    fun clear(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun put(key: String, value: Any?) = value?.let {
        val editor = preferences.edit()
        editor.putString(key, toJson(it))
        editor.apply()
    } ?: clear(key)

    fun has(key: String) = preferences.contains(key)

    fun <T : CSJsonData> load(data: T, key: String): T? {
        val loadString = loadString(key)
        if (no(loadString)) return null
        fromJson(loadString!!)?.let { data.load(it as CSMap<String, Any?>) }
        return data
    }

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

    fun loadString(key: String): String? = try {
        preferences.getString(key, null)
    } catch (ex: Exception) {
        null
    }

    fun <T : Any> loadList(key: String) = list<T>()
            .apply { (loadJson(key) as? CSList<T>)?.forEach { data -> put(data) } }

    fun <T : CSJsonData> loadDataList(type: Class<T>, key: String) =
            createList(type, loadJson(key) as CSList<CSMap<String, Any?>>?)

    private fun loadJson(key: String): Any? {
        val loadString = loadString(key)
        return if (empty(loadString)) null else fromJson(loadString!!)
    }

}
