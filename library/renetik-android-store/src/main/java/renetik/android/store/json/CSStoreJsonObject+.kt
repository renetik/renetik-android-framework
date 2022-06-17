package renetik.android.store.json

fun CSStoreJsonObject.getObject(key: String) = getMap(key)?.let(::CSStoreJsonObject)

fun CSStoreJsonObject.reload(data: Map<String, Any?>) = bulkSave().use {
    clear()
    load(data)
}