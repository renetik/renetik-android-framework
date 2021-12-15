package renetik.android.framework.json.data

import renetik.kotlin.reflect.createInstance

fun CSJsonObject.reload(data: Map<String, Any?>) = bulkSave().use {
    clear()
    load(data)
}

fun <T : CSJsonObject> T.clone() = this::class.createInstance()!!.also { it.load(this) }