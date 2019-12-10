package  renetik.android.json.data.extensions

import renetik.android.json.CSJsonList
import renetik.android.json.CSJsonMap
import renetik.android.json.data.CSJsonData

fun CSJsonData.put(key: String, value: String?) = setValue(key, value)
fun CSJsonData.put(key: String, value: Number?) = setValue(key, value)
fun CSJsonData.put(key: String, value: Boolean?) = setValue(key, value)
fun CSJsonData.put(key: String, value: CSJsonMap) = setValue(key, value.asJsonMap())
fun CSJsonData.put(key: String, value: CSJsonList) = setValue(key, value.asJsonList())
fun CSJsonData.put(key: String, value: List<*>) = setValue(key, value)
fun CSJsonData.put(key: String, value: Map<String, *>) = setValue(key, value)