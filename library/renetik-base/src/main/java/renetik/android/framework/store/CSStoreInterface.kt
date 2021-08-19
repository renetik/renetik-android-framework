package renetik.android.framework.store

import renetik.android.framework.event.property.CSPropertyStoreInterface
import renetik.android.framework.lang.CSId
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong

interface CSStoreInterface : CSPropertyStoreInterface, Iterable<Map.Entry<String, Any?>> {

    val data: Map<String, Any?>
    override fun iterator(): Iterator<Map.Entry<String, Any?>> = data.iterator()

    fun save(key: String, value: String?)
    fun load(store: CSStoreInterface)
    fun clear()

    fun get(key: String): String? = data[key]?.toString()
    fun has(key: String): Boolean = data.containsKey(key)
    fun reload(store: CSStoreInterface) {
        clear()
        load(store)
    }

    fun save(key: String, value: Int?) = save(key, value?.toString())
    fun save(key: String, value: Boolean?) = save(key, value?.toString())
    fun save(key: String, value: Float?) = save(key, value?.toString())
    fun save(key: String, value: Double?) = save(key, value?.toString())
    fun save(key: String, value: Long?) = save(key, value?.toString())

    fun getBoolean(key: String, default: Boolean) = get(key)?.toBoolean() ?: default
    fun getBoolean(key: String, default: Boolean? = null) = get(key)?.toBoolean() ?: default
    fun getDouble(key: String, default: Double) = get(key)?.asDouble() ?: default
    fun getDouble(key: String, default: Double? = null) = get(key)?.asDouble() ?: default
    fun getLong(key: String, default: Long) = get(key)?.asLong() ?: default
    fun getLong(key: String, default: Long?) = get(key)?.asLong() ?: default
    fun getFloat(key: String, default: Float) = get(key)?.asFloat() ?: default
    fun getFloat(key: String, default: Float? = null) = get(key)?.asFloat() ?: default
    fun getInt(key: String, default: Int) = get(key)?.asInt() ?: default
    fun getInt(key: String, default: Int? = null) = get(key)?.asInt() ?: default
    fun getString(key: String, default: String) = get(key) ?: default
    fun getString(key: String) = get(key)

    override fun <T> property(
        key: String, values: List<T>, default: T, onChange: ((value: T) -> Unit)?) =
        CSListItemStoreEventProperty(this, key, values, default, onChange)

    override fun <T : CSId> property(
        key: String, values: Iterable<T>, default: List<T>, onChange: ((value: List<T>) -> Unit)?) =
        CSListItemsStoreEventProperty(this, key, values, default, onChange)

    override fun property(key: String, default: Int, onChange: ((value: Int) -> Unit)?) =
        CSStoreIntEventProperty(this, key, default, onChange)

    override fun property(key: String, default: Float, onChange: ((value: Float) -> Unit)?) =
        CSStoreFloatEventProperty(this, key, default, onChange)

    override fun property(key: String, default: Double, onChange: ((value: Double) -> Unit)?) =
        CSStoreDoubleEventProperty(this, key, default, onChange)

    override fun property(key: String, default: String, onChange: ((value: String) -> Unit)?) =
        CSStoreStringEventProperty(this, key, default, onChange)

    fun storedString(key: String, onChange: ((value: String) -> Unit)? = null) =
        CSStoredStringEventProperty(this, key, onChange)

    override fun property(key: String, default: Boolean, onChange: ((value: Boolean) -> Unit)?) =
        CSStoreBooleanEventProperty(this, key, default, onChange)

    fun storedBoolean(key: String, default: Boolean? = null,
                      onChange: ((value: Boolean?) -> Unit)?) =
        CSStoreNullableBooleanEventProperty(this, key, default, onChange)

    fun storedBoolean(key: String, onChange: ((value: Boolean) -> Unit)? = null) =
        CSStoredBooleanEventProperty(this, key, onChange)

    fun storedInt(key: String, onChange: ((value: String) -> Unit)? = null) =
        CSStoredStringEventProperty(this, key, onChange)
}

