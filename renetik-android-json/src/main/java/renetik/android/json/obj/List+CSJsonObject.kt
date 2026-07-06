package renetik.android.json.obj

import renetik.android.json.createJsonObjectList
import renetik.android.json.parseJsonList
import renetik.android.json.toJson

fun <T : CSJsonObject> List<T>.clone(): List<T> = map { it.clone() }

@Suppress("UNCHECKED_CAST")
inline fun <reified T : CSJsonObject> List<T>.cloneDeep() =
    (this.toJson().parseJsonList() as? List<Map<String, Any?>>)
        ?.let { T::class.createJsonObjectList(it) }