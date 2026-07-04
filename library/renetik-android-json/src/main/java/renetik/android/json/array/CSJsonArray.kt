package renetik.android.json.array

import renetik.android.json.toJson

@Suppress("unchecked_cast")
open class CSJsonArray() : Iterable<Any?>, CSJsonArrayInterface {

    constructor(list: List<Any?>) : this() {
        load(list)
    }

    var index: Int? = null
    var key: String? = null
    internal val data = mutableListOf<Any?>()

    fun load(list: List<Any?>) = apply { data.addAll(list) }

    override fun toString() = super.toString() + toJson(formatted = true)

    override val jsonList: List<*>
        get() = data

    override fun iterator() = data.iterator()
}





