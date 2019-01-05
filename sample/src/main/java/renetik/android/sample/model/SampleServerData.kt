package renetik.android.sample.model

import renetik.android.client.request.CSServerData
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonDataListProperty
import kotlin.reflect.KClass

class ListServerData<ListItem : CSJsonData>(key: String, kClass: KClass<ListItem>) : CSServerData() {
    private val listProperty = CSJsonDataListProperty(this, kClass, key)
    val list get() = listProperty.list
}

class StringValueServerData : CSServerData() {
    val value get() = getString("value")!!
}

class PingServerData : CSServerData() {
    override val success get() = "Hello, Stranger!" == message
}