package renetik.android.framework.store

import renetik.android.framework.event.property.CSPropertyStoreInterface
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong

interface CSStoreInterface : CSPropertyStoreInterface {

    fun save(key: String, value: String?)

    fun save(key: String, value: Int?) = save(key, value?.toString())

    fun save(key: String, value: Boolean?) = save(key, value?.toString())

    fun save(key: String, value: Float?) = save(key, value?.toString())

    fun save(key: String, value: Double?) = save(key, value?.toString())

    fun save(key: String, value: Long?) = save(key, value?.toString())

    fun has(key: String): Boolean

    fun getString(key: String): String?

    fun getBoolean(key: String, defaultValue: Boolean) =
        getString(key)?.toBoolean() ?: defaultValue

    fun getDouble(key: String, defaultValue: Double) =
        getString(key)?.asDouble(defaultValue) ?: defaultValue

    fun getDouble(key: String, defaultValue: Double?) =
        getString(key)?.asDouble() ?: defaultValue

    fun getLong(key: String, defaultValue: Long) =
        getString(key)?.asLong(defaultValue) ?: defaultValue

    fun getFloat(key: String, defaultValue: Float) =
        getString(key)?.asFloat(defaultValue) ?: defaultValue

    fun getFloat(key: String, defaultValue: Float?) =
        getString(key)?.asFloat() ?: defaultValue

    fun getInt(key: String, defaultValue: Int = 0) =
        getString(key)?.asInt(defaultValue) ?: defaultValue

    fun getString(key: String, defaultValue: String) = getString(key) ?: defaultValue

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

