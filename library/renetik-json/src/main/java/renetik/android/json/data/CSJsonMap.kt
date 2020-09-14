package renetik.android.json.data

import renetik.android.json.CSJsonMapInterface
import renetik.android.json.toJsonString

@Suppress("unchecked_cast")
open class CSJsonMap() : Iterable<String>, CSJsonMapInterface {

    constructor(map: MutableMap<String, Any?>) : this() {
        load(map)
    }

    var index: Int? = null
    var key: String? = null
    internal val data = mutableMapOf<String, Any?>()

    fun load(map: Map<String, Any?>) = apply { data.putAll(map) }

    override fun toString() = toJsonString(formatted = true)

    override fun asStringMap(): Map<String, *> = data

    override fun iterator() = data.keys.iterator()
}



