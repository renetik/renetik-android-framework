package renetik.android.json

@Suppress("unchecked_cast")
open class CSJsonArray() : Iterable<Any?>, CSJsonArrayInterface {

    constructor(list: List<Any?>) : this() {
        load(list)
    }

    var index: Int? = null
    var key: String? = null
    internal val data = mutableListOf<Any?>()

    fun load(list: List<Any?>) = apply { data.addAll(list) }

    override fun toString() = super.toString() + toJsonString(formatted = true)

    override fun asList(): List<*> = data

    override fun iterator() = data.iterator()
}





