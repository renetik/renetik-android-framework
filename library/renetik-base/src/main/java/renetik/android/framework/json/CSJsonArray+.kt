package renetik.android.framework.json

fun CSJsonArray.toJsonArray() = asList().toJSONArray()

fun <T : CSJsonArray> T.load(data: String) = apply { load(data.parseJsonList()!!) }

internal fun <T : CSJsonArray> T.add(value: Any?) = apply { data.add(value) }

fun <T : CSJsonArray> T.clone(): T = //TODO: Why is this needed ?
    this::class.createJsonList(toJsonString().parseJson())