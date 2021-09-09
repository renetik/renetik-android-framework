package renetik.android.framework.json.data

import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJSONObject

fun CSJsonMapStore.toJsonObject() = asStringMap().toJSONObject()

fun <T : CSJsonMapStore> T.load(data: String) = apply { load(data.parseJsonMap()!!) }