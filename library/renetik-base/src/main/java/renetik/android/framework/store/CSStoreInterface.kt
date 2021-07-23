package renetik.android.framework.store

import renetik.android.framework.event.property.CSPropertyStoreInterface

interface CSStoreInterface : CSPropertyStoreInterface {

    fun save(key: String, value: Int?)

    fun save(key: String, value: Boolean?)

    fun save(key: String, value: Float?)

    fun save(key: String, value: Double?)

    fun save(key: String, value: Long?)

    fun save(key: String, value: String?)

    fun has(key: String): Boolean

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun getDouble(key: String, defaultValue: Double = 0.0): Double

    fun getDouble(key: String, defaultValue: Double? = null): Double?

    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun getFloat(key: String, defaultValue: Float = 0F): Float

    fun getFloat(key: String, defaultValue: Float? = null): Float?

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun getString(key: String, defaultValue: String): String

    fun getString(key: String): String?

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

