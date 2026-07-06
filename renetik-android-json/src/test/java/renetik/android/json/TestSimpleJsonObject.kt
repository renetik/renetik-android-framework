package renetik.android.json

import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.getBooleanMap
import renetik.android.json.obj.getDoubleList

data class TestSimpleJsonObject(
    var string: String? = null,
    var map: Map<String, Boolean>? = null,
    var list: List<Double>? = null) : CSJsonObject() {

    init {
        set("stringKey", string)
        set("mapKey", map)
        set("listKey", list)
    }

    override fun onLoad() {
        string = getString("stringKey")
        map = getBooleanMap("mapKey", default = false)
        list = getDoubleList("listKey", default = 0.0)
    }
}
