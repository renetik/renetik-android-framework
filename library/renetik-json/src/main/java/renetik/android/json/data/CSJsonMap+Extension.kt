package renetik.android.json.data

import renetik.android.json.parseJsonMap
import renetik.android.json.toJSONObject

fun CSJsonMap.toJsonObject() = asStringMap().toJSONObject()

fun <T : CSJsonMap> T.load(data: String) = apply { load(data.parseJsonMap()!!) }

internal fun <T : CSJsonMap> T.put(key: String, value: Any?) = apply {
    data[key] = value
}