package renetik.android.framework.json.data

fun CSJsonObject.reload(data: Map<String, Any?>) = bulkSave().use {
    clear()
    load(data)
}