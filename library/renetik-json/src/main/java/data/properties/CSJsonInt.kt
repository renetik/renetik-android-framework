package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.getInt
import renetik.android.json.extensions.put

class CSJsonInt(val data: CSJsonData, private val key: String) {
    var integer: Int?
        get() = data.getInt(key)
        set(value) = data.put(key, value)

    val value get() = integer ?: 0

    override fun toString() = "$value"
}