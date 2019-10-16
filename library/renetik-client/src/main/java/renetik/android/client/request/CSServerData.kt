package renetik.android.client.request

import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonBoolean
import renetik.android.json.data.properties.CSJsonDataList
import renetik.android.json.data.properties.CSJsonInt
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.extensions.*
import kotlin.reflect.KClass

open class CSServerData : CSJsonData() {
    open val success: Boolean get() = getBoolean("success") ?: false
    open val message: String? get() = getString("message")
}

class CSValueServerData<ValueType : CSJsonData>(key: String, kClass: KClass<ValueType>) : CSServerData() {
    constructor(kClass: KClass<ValueType>) : this("value", kClass)

    val value: ValueType by lazy { kClass.createJsonData(getMap(key)) }
}

class CSListServerData<ListItem : CSJsonData>(key: String, kClass: KClass<ListItem>) : CSServerData() {
    constructor(kClass: KClass<ListItem>) : this("list", kClass)

    private val property = CSJsonDataList(this, kClass, key)
    val list get() = property.list
}

class CSStringServerData(key: String) : CSServerData() {
    constructor() : this("value")

    private val property = CSJsonString(this, key)
    val value get() = property.value
}

class CSIntServerData(key: String) : CSServerData() {
    constructor() : this("value")

    private val property = CSJsonInt(this, key)
    val value get() = property.value
}

class CSBoolServerData(key: String) : CSServerData() {
    constructor() : this("value")

    private val property = CSJsonBoolean(this, key)
    val value get() = property.value
}