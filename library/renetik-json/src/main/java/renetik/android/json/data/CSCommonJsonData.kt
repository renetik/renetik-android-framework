package renetik.android.json.data

import renetik.android.json.extensions.getStringValue

class CSNameJsonData(private val nameKey: String) : CSJsonData() {

    constructor() : this("name")

    val id get() = getStringValue("id")

    val name get() = getStringValue(nameKey)

    override fun toString(): String {
        return name
    }
}


