package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.getDouble
import renetik.android.json.extensions.put

class CSJsonFloat(val data: CSJsonData, private val key: String) {
    var float: Float?
        get() = data.getDouble(key)?.toFloat()
        set(value) = data.put(key, value)

    val value get() = float ?: 0

    override fun toString() = "$value"
}