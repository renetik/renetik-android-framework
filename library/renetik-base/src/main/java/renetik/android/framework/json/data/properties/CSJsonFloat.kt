package renetik.android.framework.json.data.properties

import renetik.android.framework.lang.property.CSProperty
import renetik.android.framework.json.data.CSJsonObject

class CSJsonFloat(val data: CSJsonObject, private val key: String) : CSProperty<Float> {
    var float: Float?
        get() = data.getDouble(key)?.toFloat()
        set(value) {
            data.set(key, value)
        }

    override var value: Float
        get() = float ?: 0F
        set(value) {
            float = value
        }

    override fun toString() = "$value"
}