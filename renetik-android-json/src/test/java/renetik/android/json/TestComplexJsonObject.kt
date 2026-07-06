package renetik.android.json

import renetik.android.json.obj.CSJsonObject

data class TestComplexJsonObject(
    var string: String? = null,
    var map: Map<String, TestComplexJsonObject>? = null,
    var list: List<TestComplexJsonObject>? = null) : CSJsonObject() {

    init {
        string?.let { set("stringKey", it) }
        map?.let { set("mapKey", it) }
        list?.let { set("listKey", it) }
    }

    override fun onLoad() {
        string = getString("stringKey")
        map = getJsonObjectMap("mapKey", TestComplexJsonObject::class)
        list = getJsonObjectList("listKey", TestComplexJsonObject::class)
    }
}
