package renetik.android.json.data

import renetik.android.json.data.extensions.getStringValue

class CSNameData(private val nameKey: String) : CSJsonData() {

    constructor() : this("name")

    val id get() = getStringValue("id")

    val name get() = getStringValue(nameKey)

    override fun toString(): String {
        return name
    }
}


