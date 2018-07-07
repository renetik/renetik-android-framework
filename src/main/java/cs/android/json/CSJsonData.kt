package cs.android.json

import cs.java.collections.CSList
import cs.java.collections.CSMap
import cs.java.lang.CSLang.linkedMap
import cs.java.lang.CSLang.no

open class CSJsonData() : Iterable<String>, CSJsonDataMap {

    override fun getJsonDataMap(): Map<String, *> {
        return data
    }

    override fun iterator(): Iterator<String> {
        return data.keys.iterator()
    }

    protected var data: CSMap<String, Any?> = linkedMap<String, Any?>()
    open var index: Int? = null
    open var key: String? = null
    private var childDataKey: String? = null

    constructor(data: CSMap<String, Any?>) : this() {
        load(data)
    }

    fun load(data: CSMap<String, Any?>): CSJsonData {
        if (no(data)) return this
        this.data = data;
        onLoad(data)
        return this
    }

    protected fun onLoad(data: CSMap<String, Any?>) {}

    private fun data(): CSMap<String, Any?> {
        childDataKey?.let {
            var childValue = data[it] as? CSMap<String, Any?>
            if(childValue==null){
                childValue = linkedMap()
                data[it] = childValue
            }
            return childValue!!
        }
        return data
    }

    override fun toString(): String {
        return toFormattedJson(this)
    }

    fun put(key: String, value: String?) {
        data()[key] = value
    }

    fun put(key: String, value: Number?) {
        data()[key] = value
    }

    fun put(key: String, value: Boolean?) {
        data()[key] = value
    }

    fun put(key: String, value: CSJsonDataMap) {
        data()[key] = value.getJsonDataMap()
    }

    fun put(key: String, value: CSJsonDataList) {
        data()[key] = value.getJsonDataList()
    }

    fun put(key: String, value: List<*>) {
        data()[key] = value
    }

    fun put(key: String, value: Map<String, *>) {
        data()[key] = value
    }

    fun getString(key: String): String? {
        val type = data()[key]
        type?.let { return type.toString() } ?: return null
    }

    fun getDouble(key: String): Double? {
        return try {
            java.lang.Double.parseDouble(getString(key))
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun getInt(key: String): Int? {
        return try {
            java.lang.Integer.parseInt(getString(key))
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun getBoolean(key: String): Boolean? {
        return try {
            java.lang.Boolean.parseBoolean(getString(key))
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun getMap(key: String): CSMap<String, Any?>? {
        val type = data()[key]
        return type as? CSMap<String, Any?>
    }

    fun getList(key: String): CSList<Any?>? {
        val type = data()[key]
        return type as? CSList<Any?>
    }

    fun <T : CSJsonData> load(dataValue: T, data: CSMap<String, *>, key: String): T? {
        val value = data[key] as? CSMap<String, Any?>
        value?.let { dataValue.load(value) } ?: return null
        return dataValue
    }

    fun <T : CSJsonData> load(dataValue: T, key: String): T? {
        return load(dataValue, data(), key)
    }

}
