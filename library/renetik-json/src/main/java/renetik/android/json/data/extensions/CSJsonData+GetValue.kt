package  renetik.android.json.data.extensions

import renetik.android.json.data.CSJsonMap

fun CSJsonMap.getStringValue(key: String): String = getString(key) ?: ""
fun CSJsonMap.getIntValue(key: String): Int = getInt(key) ?: 0
fun CSJsonMap.getBooleanValue(key: String): Boolean = getBoolean(key) ?: false