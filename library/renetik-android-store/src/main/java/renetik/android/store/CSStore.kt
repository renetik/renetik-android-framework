package renetik.android.store

import renetik.android.core.lang.lazy.CSLazyVar.Companion.lazyVar
import renetik.android.event.CSEvent
import renetik.android.event.change.CSHasChange
import renetik.android.json.obj.CSJsonObjectInterface
import renetik.android.store.operation
import renetik.android.store.type.CSFileJsonStore.Companion.CSFileJsonStore
import renetik.android.store.type.CSJsonObjectStore

interface CSStore : Iterable<Map.Entry<String, Any?>>, CSJsonObjectInterface,
    CSHasChange<CSStore> {

    companion object {
        var fileStore: CSStore by lazyVar { CSFileJsonStore("store") }
        val runtimeStore: CSStore by lazy { CSJsonObjectStore() }

        val Empty: CSStore get() = CSJsonObjectStore()
    }

    val eventLoaded: CSEvent<CSStore>
    val eventChanged: CSEvent<CSStore>
    override fun onChange(function: (CSStore) -> Unit) = eventChanged.onChange(function)
    val data: Map<String, Any?>

    fun startOperation(): Boolean = false
    fun stopOperation() = Unit

    fun load(data: Map<String, Any?>)
    fun reload(data: Map<String, Any?>) = operation {
        clear()
        load(data)
    }
}