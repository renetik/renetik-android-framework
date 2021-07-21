package renetik.android.framework

import renetik.android.java.event.property.CSEventProperty
import renetik.android.java.event.property.CSListItemStoreEventProperty
import renetik.android.java.event.property.CSPropertyStoreInterface

interface CSValueStoreInterface : CSPropertyStoreInterface {

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

    // Start CSPropertyStoreInterface

    override fun property(
        key: String, default: Boolean, onApply: ((value: Boolean) -> Unit)?
    ) = CSEventProperty(getBoolean(key, default), onApply).apply { onChange { save(key, it) } }

    override fun <T> property(
        key: String, values: List<T>, default: T, onApply: ((value: T) -> Unit)?
    ) = CSListItemStoreEventProperty(this, key, values, default, onApply)

    override fun property(
        key: String, default: Int, onApply: ((value: Int) -> Unit)?
    ) = CSEventProperty(getInt(key, default), onApply)
        .apply { onChange { save(key, it) } }

    override fun property(
        key: String, default: Float,
        onApply: ((value: Float) -> Unit)?
    ) = CSEventProperty(getFloat(key, default), onApply)
        .apply { onChange { save(key, it) } }

    override fun property(
        key: String, default: Double,
        onApply: ((value: Double) -> Unit)?
    ) = CSEventProperty(getDouble(key, default), onApply)
        .apply { onChange { save(key, it) } }

    override fun property(
        key: String, default: String, onChange: ((value: String) -> Unit)?
    ) = CSEventProperty(getString(key, default), onChange)
        .apply { onChange { save(key, it) } }

    // End CSPropertyStoreInterface
}