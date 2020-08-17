package renetik.android.json.data

import renetik.android.java.common.CSName
import renetik.android.json.data.extensions.getStringValue

class CSNameData : CSJsonData, CSName {

    private var idKey: String = "id"
    private var nameKey: String = "name"

    constructor()

    constructor(nameKey: String) {
        this.nameKey = nameKey
    }

    constructor(idKey: String, nameKey: String, data: MutableMap<String, Any?>) {
        this.idKey = idKey
        this.nameKey = nameKey
        load(data)
    }

    constructor(data: MutableMap<String, Any?>) : super(data)

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



