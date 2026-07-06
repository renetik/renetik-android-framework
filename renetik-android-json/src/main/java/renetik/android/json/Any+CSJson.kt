package renetik.android.json

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONObject.NULL
import renetik.android.core.kotlin.asString
import renetik.android.json.array.CSJsonArrayInterface
import renetik.android.json.obj.CSJsonObjectInterface

fun Any.toJson(
    forceString: Boolean = CSJson.forceString,
    formatted: Boolean = CSJson.isJsonPretty,
): String {
    val jsonType = toJsonType(forceString)
    if (formatted) {
        if (jsonType is JSONArray) return jsonType.toString(2)
        if (jsonType is JSONObject) return jsonType.toString(2)
    }
    return java.lang.String.valueOf(jsonType)
}

@Suppress("UNCHECKED_CAST")
fun Any?.toJsonType(forceString: Boolean = CSJson.forceString): Any? {
    if (this == null) return NULL
    if (this == NULL || this is JSONObject || this is JSONArray || this is String) return this
    if (!forceString && (this is Number || this is Boolean)) return this
    return (this as? Map<String, *>)?.toJSONObject(forceString)
        ?: (this as? Array<*>)?.toJSONArray(forceString)
        ?: (this as? List<*>)?.toJSONArray(forceString)
        ?: (this as? CSJsonObjectInterface)?.jsonMap?.toJSONObject(forceString)
        ?: (this as? CSJsonArrayInterface)?.jsonList?.toJSONArray(forceString)
        ?: this.asString
}

fun List<*>.toJSONArray(forceString: Boolean = CSJson.forceString): JSONArray {
    val jsonArray = JSONArray()
    for (entry in this) jsonArray.put(entry.toJsonType(forceString))
    return jsonArray
}

fun Array<*>.toJSONArray(forceString: Boolean = CSJson.forceString): JSONArray {
    val jsonArray = JSONArray()
    for (entry in this) jsonArray.put(entry.toJsonType(forceString))
    return jsonArray
}

fun Map<String, *>.toJSONObject(forceString: Boolean = CSJson.forceString): JSONObject =
    CSJson.JSONObject().also { jsonObject ->
        entries.forEach { jsonObject.put(it.key, it.value.toJsonType(forceString)) }
    }