package renetik.android.json.data

import renetik.android.json.extensions.createJsonMap
import renetik.android.json.parseJson
import renetik.android.json.toJSONObject
import renetik.android.json.toJsonString

fun CSJsonMap.toJsonObject() = asStringMap().toJSONObject()

fun <T : CSJsonMap> T.load(data: String) = apply {
    load(data.parseJson<MutableMap<String, Any?>>()!!)
}

internal fun <T : CSJsonMap> T.put(key: String, value: Any?) = apply {
    data[key] = value
}

fun <T : CSJsonMap> T.clone(): T = // Why d fuck is this needed ?
    this::class.createJsonMap(toJsonString().parseJson())