package renetik.android.json.data

import renetik.android.java.event.event
import renetik.android.java.extensions.collections.linkedMap
import renetik.android.java.extensions.collections.map
import renetik.android.json.CSJsonMap
import renetik.android.json.extensions.createJsonData
import renetik.android.json.parseJson
import renetik.android.json.toJSONObject
import renetik.android.json.toJsonString
import renetik.android.task.doLater

data class JsonDataValueChange(val data: CSJsonData, val key: String, val value: Any?)

fun CSJsonData.toJsonObject() = asJsonMap().toJSONObject()

fun <T : CSJsonData> T.clone(): T = this::class.createJsonData(toJsonString().parseJson())

@Suppress("unchecked_cast")
open class CSJsonData : Iterable<String>, CSJsonMap {

    var index: Int? = null
    var key: String? = null
    val onValueChangedEvent = event<JsonDataValueChange>()
    val onChangedEvent = event<CSJsonData>()
    private var _data = map<String, Any?>()
    private var childDataKey: String? = null
    private var dataChanged = false

    fun load(map: MutableMap<String, Any?>): CSJsonData {
        _data = map
        return this
    }

    open val data: MutableMap<String, Any?>
        get() {
            childDataKey?.let { key ->
                var childValue = _data[key] as? MutableMap<String, Any?>
                return childValue ?: let {
                    childValue = linkedMap()
                    _data[key] = childValue
                    return childValue as MutableMap<String, Any?>
                }
            }
            return _data
        }

    open fun setValue(key: String, value: Any?) {
        data[key] = value
        onValueChangedEvent.fire(JsonDataValueChange(this, key, value))
        if (!dataChanged) {
            doLater {
                onChangedEvent.fire(this)
                dataChanged = false
            }
            dataChanged = true
        }
    }

    override fun toString() = toJsonString(formatted = true)

    override fun asJsonMap(): Map<String, *> = _data

    override fun iterator(): Iterator<String> = _data.keys.iterator()

}


