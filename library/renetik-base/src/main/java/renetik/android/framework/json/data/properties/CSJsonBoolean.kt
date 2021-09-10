package renetik.android.framework.json.data.properties

import renetik.android.framework.lang.CSProperty
import renetik.android.framework.json.data.CSJsonObject

class CSJsonBoolean(val data: CSJsonObject, private val key: String) : CSProperty<Boolean> {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) {
            data.save(key, value)
        }

    override var value
        get() = bool ?: false
        set(value) {
            bool = value
        }

    override fun toString() = "$value"
}