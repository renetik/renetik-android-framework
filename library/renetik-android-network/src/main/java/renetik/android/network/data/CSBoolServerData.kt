package renetik.android.network.data

import renetik.android.store.extensions.lateBoolProperty

class CSBoolServerData(key: String) : renetik.android.network.data.CSServerMapData() {
    constructor() : this("value")

    private val property = lateBoolProperty(key)
    val value get() = property.value
}