package renetik.android.framework.json.data.properties

import renetik.android.framework.lang.property.CSProperty
import renetik.android.framework.json.data.CSJsonObject

class CSJsonString(val data: CSJsonObject, private val key: String)
    : CSProperty<String>, CharSequence {
    var string: String?
        get() = data.getString(key)
        set(value) {
            data.set(key, value)
        }

    override var value
        get() = string ?: ""
        set(value) {
            string = value
        }

    override fun toString() = value
    override val length get() = value.length
    override fun get(index: Int) = value[index]
    override fun subSequence(startIndex: Int, endIndex: Int) =
        value.subSequence(startIndex, endIndex)

    fun value(value: String) {
        string = value
    }
}