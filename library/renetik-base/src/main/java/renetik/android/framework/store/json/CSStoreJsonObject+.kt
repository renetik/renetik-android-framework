package renetik.android.framework.json

import renetik.android.framework.store.json.CSStoreJsonObject

fun CSStoreJsonObject.getObject(key: String) = getMap(key)?.let(::CSStoreJsonObject)

fun CSStoreJsonObject.reload(data: Map<String, Any?>) = bulkSave().use {
    clear()
    load(data)
}