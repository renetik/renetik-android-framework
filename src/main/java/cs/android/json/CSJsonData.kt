package cs.android.json

import cs.java.collections.CSList
import cs.java.collections.CSMap
import cs.java.event.CSEvent
import cs.java.lang.CSLang.*

data class OnJsonDataValueChanged(val data:CSJsonData, val key: String, val value: Any?)

open class CSJsonData() : Iterable<String>, CSJsonDataMap {

    override fun getJsonDataMap(): Map<String, *> {
        return data
    }

    override fun iterator(): Iterator<String> {
        return data.keys.iterator()
    }

    open var index: Int? = null
    open var key: String? = null
    open val onValueChangedEvent: CSEvent<OnJsonDataValueChanged> = event()
    open val onChangedEvent: CSEvent<CSJsonData> = event()
    protected var data: CSMap<String, Any?> = linkedMap<String, Any?>()
    private var childDataKey: String? = null
    private var dataChanged = NO

    constructor(data: CSMap<String, Any?>) : this() {
        load(data)
    }

    fun load(data: CSMap<String, Any?>): CSJsonData {
        if (no(data)) return this
        this.data = data;
        return this
    }

    private fun data(): CSMap<String, Any?> {
        childDataKey?.let {
            var childValue = data[it] as CSMap<String, Any?>
            if (childValue == null) {
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

    fun setValue(key: String, value: Any?) {
        data()[key] = value
        onValueChangedEvent.fire(OnJsonDataValueChanged(this,key,value))
        if (!dataChanged) {
            doLater {
                onChangedEvent.fire(this)
                dataChanged = NO
            }
            dataChanged = YES
        }
    }

    fun put(key: String, value: String?) {
        setValue(key, value)
    }

    fun put(key: String, value: Number?) {
        setValue(key, value)
    }

    fun put(key: String, value: Boolean?) {
        setValue(key, value)
    }

    fun put(key: String, value: CSJsonDataMap) {
        setValue(key, value.getJsonDataMap())
    }

    fun put(key: String, value: CSJsonDataList) {
        setValue(key, value.getJsonDataList())
    }

    fun put(key: String, value: List<*>) {
        setValue(key, value)
    }

    fun put(key: String, value: Map<String, *>) {
        setValue(key, value)
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
