package  renetik.android.framework.json.data.extensions

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getList
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.putAll
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.extensions.createJsonDataList
import renetik.android.framework.json.extensions.createJsonMap
import renetik.android.framework.json.parseJson
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("unchecked_cast")
fun <T : CSJsonMapStore> CSStoreInterface.get(type: KClass<T>, key: String) =
    (data[key] as? MutableMap<String, Any?>)?.let { type.createJsonMap(it) }

@Suppress("unchecked_cast")
fun <T : CSJsonMapStore> CSStoreInterface.getList(type: KClass<T>, key: String) =
    (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonDataList(it) }

fun <T : CSJsonMapStore> CSStoreInterface.getList(
    createInstance: () -> T, key: String, default: List<T>? = null): MutableList<T> {
    val list = list<T>()
    val data = get(key)?.parseJson<List<Map<String, Any?>>?>()
    data?.withIndex()?.forEach { (index, itemData) ->
        val itemInstance = createInstance()
        itemInstance.load(itemData)
        itemInstance.index = index
        list.put(itemInstance)
    } ?: default?.let { list.putAll(default) }
    return list
}

@Suppress("unchecked_cast")
fun <T : CSJsonMapStore> CSStoreInterface.getListOfList(key: String, type: KClass<T>)
        : MutableList<List<T>> {
    val playsData = getList(key)!!
    val plays = mutableListOf<List<T>>()
    for (keyPlayData in playsData) {
        val keyPlay = mutableListOf<T>()
        for (playNoteData in keyPlayData as List<Map<String, Any?>>) {
            val notePlay = type.createInstance()
            notePlay.load(playNoteData)
            keyPlay.add(notePlay)
        }
        plays.add(keyPlay)
    }
    return plays
}