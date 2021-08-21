package  renetik.android.json.data.extensions

import renetik.android.json.CSJsonListInterface
import renetik.android.json.CSJsonMapInterface
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.put

fun <T : CSJsonMap> T.put(key: String, value: String?) = put(key, value)
fun <T : CSJsonMap> T.put(key: String, value: Number?) = put(key, value)
fun <T : CSJsonMap> T.put(key: String, value: Boolean?) = put(key, value)
fun <T : CSJsonMap> T.put(key: String, value: CSJsonMapInterface) = put(key, value.asStringMap())
fun <T : CSJsonMap> T.put(key: String, value: CSJsonListInterface) = put(key, value.asList())
fun <T : CSJsonMap> T.put(key: String, value: List<*>) = put(key, value)
fun <T : CSJsonMap> T.put(key: String, value: Map<String, *>) = put(key, value)