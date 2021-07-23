package renetik.android.framework.event.property

import renetik.android.framework.store.CSListItemStoreEventProperty

interface CSPropertyStoreInterface {
    fun property(
        key: String, default: String, onChange: ((value: String) -> Unit)? = null
    ): CSEventProperty<String>

    fun property(
        key: String, default: Boolean, onChange: ((value: Boolean) -> Unit)? = null
    ): CSEventProperty<Boolean>

    fun property(
        key: String, default: Int, onChange: ((value: Int) -> Unit)? = null
    ): CSEventProperty<Int>

    fun property(
        key: String, default: Double,
        onChange: ((value: Double) -> Unit)? = null
    ): CSEventProperty<Double>

    fun property(
        key: String, default: Float,
        onChange: ((value: Float) -> Unit)? = null
    ): CSEventProperty<Float>

    fun <T> property(
        key: String, values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
    ): CSListItemStoreEventProperty<T>

    fun <T> property(
        key: String, values: Array<T>, default: T, onChange: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), default, onChange)

    fun <T : Enum<T>> property(
        key: String, values: Array<T>, default: T, onChange: (value: T) -> Unit
    ) = property(key, values, default.ordinal, onChange)

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int = 0,
        onChange: ((value: T) -> Unit)? = null
    ) = property(key, values, values[defaultIndex], onChange)

    fun <T> property(
        key: String, values: Array<T>, defaultIndex: Int = 0,
        onChange: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), values[defaultIndex], onChange)
}