package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.getBoolean
import renetik.android.json.extensions.put

class CSJsonBoolean(val data: CSJsonData, private val key: String) {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) = data.put(key, value)

    val value get() = bool ?: false

    override fun toString() = "$value"
}