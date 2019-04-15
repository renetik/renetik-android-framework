package renetik.android.json.data.properties

import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.getString
import renetik.android.json.extensions.put

class CSJsonString(val data: CSJsonData, private val key: String) : CharSequence {
    var string: String?
        get() = data.getString(key)
        set(value) = data.put(key, value)

    val value get() = string ?: ""
    override fun toString() = value
    override val length get() = value.length
    override fun get(index: Int) = value[index]
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)
}