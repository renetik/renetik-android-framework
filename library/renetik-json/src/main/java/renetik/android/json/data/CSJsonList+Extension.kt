package renetik.android.json.data

import renetik.android.json.extensions.createJsonList
import renetik.android.json.parseJson
import renetik.android.json.toJSONArray
import renetik.android.json.toJsonString

fun CSJsonList.toJsonArray() = asList().toJSONArray()

fun <T : CSJsonList> T.load(data: String) = apply {
    load(data.parseJson<List<Any?>>()!!)
}

internal fun <T : CSJsonList> T.add(value: Any?) = apply {
    data.add(value)
}

fun <T : CSJsonList> T.clone(): T = //Why d fuck is this needed ?
    this::class.createJsonList(toJsonString().parseJson())