package renetik.android.json.data

import renetik.android.json.parseJsonMap
import renetik.android.json.toJSONObject

fun CSJsonMapStore.toJsonObject() = asStringMap().toJSONObject()

fun <T : CSJsonMapStore> T.load(data: String) = apply { load(data.parseJsonMap()!!) }