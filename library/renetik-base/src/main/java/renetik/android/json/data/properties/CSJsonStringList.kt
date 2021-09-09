package renetik.android.json.data.properties

import renetik.android.framework.store.getList
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.last
import renetik.android.java.extensions.collections.list
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.data.extensions.getList

@Suppress("unchecked_cast")
class CSJsonStringList(val data: CSJsonMapStore, val key: String) : Iterable<String> {
    override fun iterator() = list.iterator()

    var list: List<String>
        get() = data.getList(key) as List<String>
        set(list) {
            data.save(key, list)
        }

    val last: String? get() = list.last
    val empty get() = size() == 0

    fun add(item: String) = data.getList(key)?.add(item)
        ?: data.save(key, list(item))

    fun remove(item: String) = data.getList(key)?.delete(item)
    fun removeLast() = data.getList(key)?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}