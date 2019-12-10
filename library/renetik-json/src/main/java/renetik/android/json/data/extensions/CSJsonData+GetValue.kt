package  renetik.android.json.data.extensions

import renetik.android.json.data.CSJsonData

fun CSJsonData.getStringValue(key: String): String = getString(key) ?: ""
fun CSJsonData.getIntValue(key: String): Int = getInt(key) ?: 0
fun CSJsonData.getBooleanValue(key: String): Boolean = getBoolean(key) ?: false