package renetik.android.core.lang

@Suppress("UNCHECKED_CAST")
class CSAssociation {
    private val dictionary: MutableMap<String, Any?> by lazy { mutableMapOf() }

    fun <T> value(key: String): T? = dictionary[key] as? T

    fun <T> add(key: String, value: T?): T? =
        value.also { dictionary[key] = it }

    fun <T> value(key: String, onCreate: () -> T): T =
        value(key = key) ?: add(key, onCreate())!!
}