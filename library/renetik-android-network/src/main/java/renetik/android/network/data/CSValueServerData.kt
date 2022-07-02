package renetik.android.network.data

import renetik.android.json.createJsonObject
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSValueServerData<ValueType : CSJsonObjectStore>(key: String, kClass: KClass<ValueType>) :
    CSServerMapData() {
    constructor(kClass: KClass<ValueType>) : this("value", kClass)

    val value: ValueType by lazy { kClass.createJsonObject(getMap(key)) }
}