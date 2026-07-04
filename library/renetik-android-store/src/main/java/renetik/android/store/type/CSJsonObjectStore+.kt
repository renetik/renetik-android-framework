package renetik.android.store.type

import renetik.android.core.kotlin.changeIf
import renetik.android.core.kotlin.collections.reload
import renetik.android.json.parseJsonMap
import renetik.android.json.toJson

fun CSJsonObjectStore.toJson(isPretty: Boolean): String =
    data.changeIf(isPretty) { toSortedMap() }
        .toJson(formatted = isPretty)

fun CSJsonObjectStore.loadJson(value: String) {
    value.parseJsonMap()?.also { data.reload(it) }
}