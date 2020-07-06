package renetik.android.json.data

import renetik.android.java.common.CSName
import renetik.android.json.data.extensions.getStringValue

private val idKey = "id"

class CSNameData(private val nameKey: String) : CSJsonData(), CSName {

    constructor() : this("name")

    constructor(id: String, name: String) : this() {
        setValue(idKey, id)
        setValue(nameKey, name)
    }

    val id get() = getStringValue(idKey)

    override val name get() = getStringValue(nameKey)

    override fun toString(): String {
        return name
    }
}


