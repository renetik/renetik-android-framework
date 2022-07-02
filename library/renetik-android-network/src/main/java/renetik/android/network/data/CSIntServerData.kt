package renetik.android.network.data

import renetik.android.store.extensions.lateIntProperty

class CSIntServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = lateIntProperty(key)
    val value get() = property.value
}