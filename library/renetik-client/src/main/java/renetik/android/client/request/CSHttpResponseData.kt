package renetik.android.client.request

import renetik.android.framework.json.CSJsonArray
import renetik.android.framework.json.CSJsonObject
import renetik.android.framework.json.createJsonObject
import renetik.android.framework.json.parseJsonList
import renetik.android.framework.json.parseJsonMap
import kotlin.reflect.KClass

interface CSHttpResponseData {
    fun onHttpResponse(code: Int, message: String, content: String?)
    val success: Boolean
    val message: String?
}

const val PARSING_FAILED = "Parsing data as json failed"

open class CSServerMapData : CSJsonObject(), CSHttpResponseData {
    var code: Int? = null

    override fun onHttpResponse(code: Int, message: String, content: String?) {
        content?.parseJsonMap()?.let {
            load(it)
        } ?: set("message", PARSING_FAILED)
    }

    override val success: Boolean get() = getBoolean("success") ?: false
    override val message: String? get() = getString("message")
}

open class CSServerListData : CSJsonArray(), CSHttpResponseData {

    private var _message = ""

    override fun onHttpResponse(code: Int, message: String, content: String?) {
        content?.parseJsonList()?.let {
            load(it)
        } ?: let {
            _message = PARSING_FAILED
        }
    }

    override val success: Boolean get() = message != PARSING_FAILED
    override val message: String? get() = _message
}

class CSValueServerData<ValueType : CSJsonObject>(key: String, kClass: KClass<ValueType>) :
    CSServerMapData() {
    constructor(kClass: KClass<ValueType>) : this("value", kClass)

    val value: ValueType by lazy { kClass.createJsonObject(getMap(key)) }
}

class CSStringServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = lateStringProperty(key)
    val value get() = property.value
}

class CSIntServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = lateIntProperty(key)
    val value get() = property.value
}

class CSBoolServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = lateBoolProperty(key)
    val value get() = property.value
}