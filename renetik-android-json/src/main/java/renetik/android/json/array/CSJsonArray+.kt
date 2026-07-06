package renetik.android.json.array

import org.json.JSONArray
import renetik.android.json.createJsonList
import renetik.android.json.parseJson
import renetik.android.json.parseJsonList
import renetik.android.json.toJSONArray
import renetik.android.json.toJson

fun CSJsonArray.toJsonArray(): JSONArray = jsonList.toJSONArray()

fun <T : CSJsonArray> T.load(data: String) = apply { load(data.parseJsonList()!!) }

internal fun <T : CSJsonArray> T.add(value: Any?) = apply { data.add(value) }

fun <T : CSJsonArray> T.clone(): T = this::class.createJsonList(toJson().parseJson())