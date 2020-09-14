package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.extensions.getBoolean
import renetik.android.json.data.extensions.put

class CSJsonBoolean(val data: CSJsonMap, private val key: String) {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) {
            data.put(key, value)
        }

    var value
        get() = bool ?: false
        set(value) {
            bool = value
        }

    override fun toString() = "$value"
}