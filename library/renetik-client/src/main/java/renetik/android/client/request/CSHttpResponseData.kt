package renetik.android.client.request

import renetik.android.framework.store.getMap
import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.data.properties.CSJsonBoolean
import renetik.android.json.data.properties.CSJsonDataList
import renetik.android.json.data.properties.CSJsonInt
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.extensions.createJsonMap
import renetik.android.json.parseJsonList
import renetik.android.json.parseJsonMap
import kotlin.reflect.KClass

interface CSHttpResponseData {
    fun onHttpResponse(code: Int, message: String, content: String?)
    val success: Boolean
    val message: String?
}

const val PARSING_FAILED = "Parsing data as json failed"

open class CSServerMapData : CSJsonMapStore(), CSHttpResponseData {
    var code: Int? = null

    override fun onHttpResponse(code: Int, message: String, content: String?) {
        content?.parseJsonMap()?.let {
            load(it)
        } ?: save("message", PARSING_FAILED)
    }

    override val success: Boolean get() = getBoolean("success") ?: false
    override val message: String? get() = getString("message")
}

open class CSServerListData : CSJsonList(), CSHttpResponseData {

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

class CSValueServerData<ValueType : CSJsonMapStore>(key: String, kClass: KClass<ValueType>) :
    CSServerMapData() {
    constructor(kClass: KClass<ValueType>) : this("value", kClass)

    val value: ValueType by lazy { kClass.createJsonMap(getMap(key)) }
}

class CSListServerData<ListItem : CSJsonMapStore>(key: String, kClass: KClass<ListItem>) :
    CSServerMapData() {
    constructor(kClass: KClass<ListItem>) : this("list", kClass)

    private val property = CSJsonDataList(this, kClass, key)
    val list get() = property.list
}

class CSStringServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = CSJsonString(this, key)
    val value get() = property.value
}

class CSIntServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = CSJsonInt(this, key)
    val value get() = property.value
}

class CSBoolServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = CSJsonBoolean(this, key)
    val value get() = property.value
}