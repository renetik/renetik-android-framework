package renetik.android.json.data

import renetik.android.framework.lang.CSTitle
import renetik.android.json.data.extensions.getStringValue

class CSNameData(val idKey: String = "id", val nameKey: String = "name",
                 data: MutableMap<String, Any?>? = null) : CSJsonMap(), CSTitle {
    init {
        data?.let { load(it) }
    }

    constructor(id: String, name: String) : this() {
        put(idKey, id)
        put(nameKey, name)
    }

    val id get() = getStringValue(idKey)

    override val title get() = getStringValue(nameKey)

    override fun toString(): String {
        return title
    }
}



