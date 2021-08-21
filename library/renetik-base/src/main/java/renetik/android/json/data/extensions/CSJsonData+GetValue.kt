package  renetik.android.json.data.extensions

import renetik.android.json.data.CSJsonMap

fun CSJsonMap.getStringValue(key: String, default: String = "") = getString(key) ?: default
fun CSJsonMap.getIntValue(key: String, default: Int = 0) = getInt(key) ?: default
fun CSJsonMap.getBooleanValue(key: String, default: Boolean = false) = getBoolean(key) ?: default