package cs.android.model

import android.content.Context
import cs.android.extensions.primitives.asDouble
import cs.android.extensions.primitives.asFloat
import cs.android.extensions.primitives.asInt
import cs.android.extensions.primitives.asLong
import cs.android.json.CSJsonData
import cs.android.json.createList
import cs.android.json.fromJson
import cs.android.json.toJson
import cs.android.viewbase.CSContextController
import cs.java.collections.CSList
import cs.java.collections.CSMap
import cs.java.lang.CSLang.*
import kotlin.reflect.KClass


@Suppress("unchecked_cast")
class CSValueStore(name: String) : CSContextController() {

    private val preferences = context().getSharedPreferences(name, Context.MODE_PRIVATE)

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

    fun <T : Any> loadList(key: String): CSList<T> {
        val list = list<T>()
        val dataList = loadJson(key) as CSList<T?>?
        dataList?.forEach { data -> list.put(data) }
        return list
    }

    fun <T : CSJsonData> loadDataList(type: Class<T>, key: String) =
            createList(type, loadJson(key) as CSList<CSMap<String, Any?>>?)

    private fun loadJson(key: String): Any? {
        val loadString = loadString(key)
        return if (empty(loadString)) null else fromJson(loadString!!)
    }

}
