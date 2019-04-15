package renetik.android.json.data.properties

import renetik.android.java.common.CSSizeInterface
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.last
import renetik.android.java.extensions.collections.list
import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.createJsonDataList
import renetik.android.json.extensions.getList
import renetik.android.json.extensions.put
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
class CSJsonDataList<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                     private val key: String) : Iterable<T>, CSSizeInterface {
    override fun iterator() = list.iterator()

    var list: List<T>
        get() = type.createJsonDataList(data.getList(key) as List<MutableMap<String, Any?>>?)
        set(list) {
            data.getList(key)?.clear()
            list.forEach { item -> add(item) }
        }

    val last: T? get() = list.last
    val isEmpty get() = size == 0
    override val size get() = data.getList(key)?.size ?: let { 0 }

    fun add(item: T): T {
        data.getList(key)?.add(item.asJsonMap())
                ?: data.put(key, list(item.asJsonMap()))
        return item
    }

    fun remove(item: T) = data.getList(key)?.delete(item.asJsonMap())
    fun removeLast() = data.getList(key)?.deleteLast()
    fun clear() = data.getList(key)?.clear()
    fun add(vararg items: T) = items.forEach { add(it) }
}