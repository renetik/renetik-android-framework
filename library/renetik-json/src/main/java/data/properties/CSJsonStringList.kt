package renetik.android.json.data.properties

import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.last
import renetik.android.java.extensions.collections.list
import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.getList
import renetik.android.json.extensions.put

@Suppress("unchecked_cast")
class CSJsonStringList(val data: CSJsonData, val key: String) : Iterable<String> {
    override fun iterator() = list.iterator()

    var list: List<String>
        get() = data.getList(key) as List<String>
        set(list) = data.put(key, list)

    val last: String? get() = list.last
    val empty get() = size() == 0

    fun add(item: String) = data.getList(key)?.add(item)
            ?: data.put(key, list(item))

    fun remove(item: String) = data.getList(key)?.delete(item)
    fun removeLast() = data.getList(key)?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}