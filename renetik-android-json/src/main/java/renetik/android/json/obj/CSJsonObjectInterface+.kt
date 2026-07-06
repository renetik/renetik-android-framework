package renetik.android.json.obj

import renetik.android.core.kotlin.asBoolean
import renetik.android.core.kotlin.asDouble
import renetik.android.core.kotlin.asFloat
import renetik.android.core.kotlin.asInt
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.reflect.createInstance
import renetik.android.core.kotlin.toId
import kotlin.reflect.KClass

fun CSJsonObjectInterface.getStringList(key: String): List<String>? =
    getList(key)?.map { it.asString }

fun CSJsonObjectInterface.getIntList(key: String): List<Int?>? =
    getList(key)?.map { it?.asInt() }

fun CSJsonObjectInterface.getFloatList(key: String): List<Float?>? =
    getList(key)?.map { it?.asFloat() }

fun CSJsonObjectInterface.getDoubleList(key: String): List<Double?>? =
    getList(key)?.map { it?.asDouble() }

fun CSJsonObjectInterface.getDoubleList(key: String, default: Double)
        : List<Double>? = getList(key)?.map { it?.asDouble() ?: default }

fun CSJsonObjectInterface.getArray(key: String): Array<*>? =
    getList(key)?.toTypedArray()

fun CSJsonObjectInterface.getStringMap(key: String): Map<String, String>? =
    getMap(key)?.mapValues { it.value.asString }

fun CSJsonObjectInterface.getBooleanMap(key: String): Map<String, Boolean?>? =
    getMap(key)?.mapValues { it.value?.asBoolean() }

fun CSJsonObjectInterface.getBooleanMap(key: String, default: Boolean)
        : Map<String, Boolean>? =
    getMap(key)?.mapValues { it.value?.asBoolean() ?: default }

fun CSJsonObjectInterface.getIntMap(key: String): Map<String, Int?>? =
    getMap(key)?.mapValues { it.value?.asInt() }

fun CSJsonObjectInterface.getFloatMap(key: String): Map<String, Float?>? =
    getMap(key)?.mapValues { it.value?.asFloat() }

fun <T> CSJsonObjectInterface.getValue(key: String, values: Iterable<T>): T? {
    val savedString = getString(key) ?: return null
    return values.find { it.toId() == savedString }
}

@Suppress("unchecked_cast")
fun <T : CSJsonObject> CSJsonObjectInterface.getJsonListList(
    key: String, type: KClass<T>): MutableList<List<T>>? {
    val storedList = getList(key) ?: return null
    val createdList = mutableListOf<List<T>>()
    for (storedChildList in storedList) {
        val createdChildList = mutableListOf<T>()
        for (storedChildListItem in storedChildList as List<Map<String, Any?>>) {
            val createdChildListItem = type.createInstance()!!
            createdChildListItem.load(storedChildListItem)
            createdChildList.add(createdChildListItem)
        }
        createdList.add(createdChildList)
    }
    return createdList
}

inline fun <reified T : CSJsonObject> CSJsonObjectInterface.getJsonObject(key: String): T? =
    getJsonObject(key, T::class)

inline fun <reified T : CSJsonObject> CSJsonObjectInterface.getJsonObjectList(
    key: String): List<T>? = getJsonObjectList(key, T::class)