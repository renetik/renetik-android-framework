package renetik.android.json

import renetik.java.collections.CSList
import renetik.java.collections.CSMap
import renetik.java.collections.linkedMap
import renetik.java.event.CSEvent
import renetik.java.event.event
import renetik.android.lang.doLater
import renetik.android.view.base.CSContextController

data class OnJsonDataValueChanged(val data: CSJsonData, val key: String, val value: Any?)

@Suppress("unchecked_cast")
open class CSJsonData() : CSContextController(), Iterable<String>, CSJsonDataMap {

    override fun getJsonDataMap(): Map<String, *> = data

    override fun iterator(): Iterator<String> = data.keys.iterator()

    open var index: Int? = null
    open var key: String? = null
    open val onValueChangedEvent: CSEvent<OnJsonDataValueChanged> = event()
    open val onChangedEvent: CSEvent<CSJsonData> = event()
    protected var data: CSMap<String, Any?> = linkedMap<String, Any?>()
    private var childDataKey: String? = null
    private var dataChanged = false

    constructor(data: CSMap<String, Any?>) : this() {
        load(data)
    }

    fun load(data: CSMap<String, Any?>): CSJsonData {
        this.data = data
        return this
    }

    private fun data(): CSMap<String, Any?> {
        childDataKey?.let { key ->
            var childValue = data[key] as? CSMap<String, Any?>
            return childValue ?: let {
                childValue = linkedMap()
                data[key] = childValue
                return childValue as CSMap<String, Any?>
            }
        }
        return data
    }

    override fun toString(): String {
        return toFormattedJson(this)
    }

    fun setValue(key: String, value: Any?) {
        data()[key] = value
        onValueChangedEvent.fire(OnJsonDataValueChanged(this, key, value))
        if (!dataChanged) {
            doLater {
                onChangedEvent.fire(this)
                dataChanged = false
            }
            dataChanged = true
        }
    }

    fun put(key: String, value: String?) = setValue(key, value)

    fun put(key: String, value: Number?) = setValue(key, value)

    fun put(key: String, value: Boolean?) = setValue(key, value)

    fun put(key: String, value: CSJsonDataMap) = setValue(key, value.getJsonDataMap())

    fun put(key: String, value: CSJsonDataList) = setValue(key, value.getJsonDataList())

    fun put(key: String, value: List<*>) = setValue(key, value)

    fun put(key: String, value: Map<String, *>) = setValue(key, value)

    fun getString(key: String): String? = data()[key]?.let { return it.toString() }

    fun getDouble(key: String) = try {
        getString(key)?.toDouble()
    } catch (e: NumberFormatException) {
        null
    }

    fun getInt(key: String) = try {
        getString(key)?.toInt()
    } catch (e: NumberFormatException) {
        null
    }

    fun getBoolean(key: String) = try {
        getString(key)?.toBoolean()
    } catch (e: NumberFormatException) {
        null
    }

    fun getMap(key: String) = data()[key] as? CSMap<String, Any?>

    fun getList(key: String) = data()[key] as? CSList<Any?>

    fun <T : CSJsonData> load(dataValue: T, data: CSMap<String, *>, key: String): T? {
        (data[key] as? CSMap<String, Any?>)?.let { dataValue.load(it) } ?: return null
        return dataValue
    }

    fun <T : CSJsonData> load(dataValue: T, key: String) = load(dataValue, data(), key)

}
