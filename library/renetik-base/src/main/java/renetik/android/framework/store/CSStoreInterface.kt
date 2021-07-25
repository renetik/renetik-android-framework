package renetik.android.framework.store

import renetik.android.framework.event.property.CSPropertyStoreInterface
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong

interface CSStoreInterface : CSPropertyStoreInterface {

    val data: Map<String, Any?>
    fun get(key: String): String?
    fun has(key: String): Boolean
    fun save(key: String, value: String?)
    fun load(store: CSStoreInterface)
    fun reload(store: CSStoreInterface)
    fun clear()

    fun save(key: String, value: Int?) = save(key, value?.toString())
    fun save(key: String, value: Boolean?) = save(key, value?.toString())
    fun save(key: String, value: Float?) = save(key, value?.toString())
    fun save(key: String, value: Double?) = save(key, value?.toString())
    fun save(key: String, value: Long?) = save(key, value?.toString())

    fun getBoolean(key: String, default: Boolean) = get(key)?.toBoolean() ?: default
    fun getDouble(key: String, default: Double) = get(key)?.asDouble(default) ?: default
    fun getDouble(key: String, default: Double?) = get(key)?.asDouble() ?: default
    fun getLong(key: String, default: Long) = get(key)?.asLong(default) ?: default
    fun getFloat(key: String, default: Float) = get(key)?.asFloat(default) ?: default
    fun getFloat(key: String, default: Float?) = get(key)?.asFloat() ?: default
    fun getInt(key: String, default: Int = 0) = get(key)?.asInt(default) ?: default
    fun getString(key: String, default: String) = get(key) ?: default

    override fun property(key: String, default: Boolean, onChange: ((value: Boolean) -> Unit)?) =
        CSStoreBooleanEventProperty(this, key, default, onChange)

    override fun <T> property(
        key: String, values: List<T>, default: T, onChange: ((value: T) -> Unit)?) =
        CSListItemStoreEventProperty(this, key, values, default, onChange)

    override fun property(key: String, default: Int, onChange: ((value: Int) -> Unit)?) =
        CSStoreIntEventProperty(this, key, default, onChange)

    override fun property(key: String, default: Float, onChange: ((value: Float) -> Unit)?) =
        CSStoreFloatEventProperty(this, key, default, onChange)

    override fun property(key: String, default: Double, onChange: ((value: Double) -> Unit)?) =
        CSStoreDoubleEventProperty(this, key, default, onChange)

    override fun property(key: String, default: String, onChange: ((value: String) -> Unit)?) =
        CSStoreStringEventProperty(this, key, default, onChange)
}

