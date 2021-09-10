package renetik.android.framework.json.data.properties

import renetik.android.framework.lang.CSProperty
import renetik.android.framework.json.data.CSJsonObject

class CSJsonInt(
    val data: CSJsonObject,
    private val key: String,
    private val defaultValue: Int = 0
) : CSProperty<Int> {
    var integer: Int?
        get() = data.getInt(key)
        set(value) {
            data.save(key, value)
        }

    override var value
        get() = integer ?: defaultValue
        set(value) {
            integer = value
        }


    override fun toString() = "$value"
}