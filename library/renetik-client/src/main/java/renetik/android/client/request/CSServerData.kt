package renetik.android.client.request

import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.extensions.getBoolean
import renetik.android.json.data.extensions.getMap
import renetik.android.json.data.extensions.getString
import renetik.android.json.data.extensions.put
import renetik.android.json.data.properties.CSJsonBoolean
import renetik.android.json.data.properties.CSJsonDataList
import renetik.android.json.data.properties.CSJsonInt
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.extensions.createJsonMap
import renetik.android.json.parseJsonList
import renetik.android.json.parseJsonMap
import kotlin.reflect.KClass

interface CSServerData {
    fun loadHttp(code: Int, message: String, content: String)
    val success: Boolean
    val message: String?
}

const val PARSING_FAILED = "Parsing data as json failed"

open class CSServerMapData : CSJsonMap(), CSServerData {
    var code: Int? = null

    override fun loadHttp(code: Int, message: String, content: String) {
        content.parseJsonMap()?.let {
            load(it)
        } ?: put("message", PARSING_FAILED)
    }

    override val success: Boolean get() = getBoolean("success") ?: false
    override val message: String? get() = getString("message")
}

open class CSServerListData : CSJsonList(), CSServerData {

    private var _message = ""

    override fun loadHttp(code: Int, message: String, content: String) {
        content.parseJsonList()?.let {
            load(it)
        } ?: let {
            _message = PARSING_FAILED
        }
    }

    override val success: Boolean get() = message != PARSING_FAILED
    override val message: String? get() = _message
}

class CSValueServerData<ValueType : CSJsonMap>(key: String, kClass: KClass<ValueType>) :
    CSServerMapData() {
    constructor(kClass: KClass<ValueType>) : this("value", kClass)

    val value: ValueType by lazy { kClass.createJsonMap(getMap(key)) }
}

class CSListServerData<ListItem : CSJsonMap>(key: String, kClass: KClass<ListItem>) :
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