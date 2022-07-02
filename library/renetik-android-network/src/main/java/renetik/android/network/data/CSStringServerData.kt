package renetik.android.network.data

import renetik.android.store.extensions.lateStringProperty

class CSStringServerData(key: String) : CSServerMapData() {
    constructor() : this("value")

    private val property = lateStringProperty(key)
    val value get() = property.value
}