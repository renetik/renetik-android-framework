package renetik.android.framework.json.data

import renetik.android.framework.json.extensions.createJsonList
import renetik.android.framework.json.parseJson
import renetik.android.framework.json.parseJsonList
import renetik.android.framework.json.toJSONArray
import renetik.android.framework.json.toJsonString

fun CSJsonList.toJsonArray() = asList().toJSONArray()

fun <T : CSJsonList> T.load(data: String) = apply { load(data.parseJsonList()!!) }

internal fun <T : CSJsonList> T.add(value: Any?) = apply {
    data.add(value)
}

fun <T : CSJsonList> T.clone(): T = //Why d fuck is this needed ?
    this::class.createJsonList(toJsonString().parseJson())