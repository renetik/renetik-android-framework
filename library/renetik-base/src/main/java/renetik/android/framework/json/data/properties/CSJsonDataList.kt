package renetik.android.framework.json.data.properties

import renetik.android.framework.lang.CSSizeInterface
import renetik.android.framework.store.getList
import renetik.android.java.extensions.collections.*
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.extensions.createJsonDataList
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
class CSJsonDataList<T : CSJsonMapStore>(
    val data: CSJsonMapStore, private val type: KClass<T>,
    private val key: String
) : Iterable<T>, CSSizeInterface {
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
        data.getList(key)?.add(item.asStringMap())
            ?: data.save(key, list(item.asStringMap()))
        return item
    }

    fun put(item: T): T = add(item)
    fun remove(item: T) = data.getList(key)?.delete(item.asStringMap())
    fun removeAt(index: Int) = data.getList(key)?.removeAt(index)
    fun removeLast() = data.getList(key)?.deleteLast()
    fun removeRange(fromIndex: Int) = data.getList(key)?.removeRange(fromIndex)
    fun clear() = data.getList(key)?.clear()
    fun add(vararg items: T) = items.forEach { add(it) }
}