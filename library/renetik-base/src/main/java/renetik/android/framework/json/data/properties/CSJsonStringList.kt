package renetik.android.framework.json.data.properties

import renetik.android.framework.json.data.CSJsonObject
import renetik.kotlin.collections.delete
import renetik.kotlin.collections.deleteLast
import renetik.kotlin.collections.last
import renetik.kotlin.collections.list

@Suppress("unchecked_cast")
class CSJsonStringList(val data: CSJsonObject, val key: String) : Iterable<String> {
    override fun iterator() = list.iterator()

    private val dataList get() = data.getList(key) as? MutableList<String>

    var list: List<String>
        get() = dataList!!
        set(list) {
            data.save(key, list)
        }

    val last: String? get() = list.last
    val empty get() = size() == 0

    fun add(item: String) = dataList?.add(item)
        ?: data.save(key, list(item))

    fun remove(item: String) = dataList?.delete(item)
    fun removeLast() = dataList?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}