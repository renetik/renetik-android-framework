package renetik.android.framework.event.property

import renetik.android.framework.lang.CSHasId
import renetik.android.framework.store.property.nullable.CSListItemNullableStoreEventProperty
import renetik.android.framework.store.property.value.CSListItemValueStoreEventProperty
import renetik.android.framework.store.property.value.CSListValueStoreEventProperty

interface CSPropertyStoreInterface {
    fun property(
        key: String, value: String,
        onChange: ((value: String) -> Unit)? = null
    ): CSEventProperty<String>

    fun property(key: String, value: Boolean,
                 onChange: ((value: Boolean) -> Unit)? = null)
            : CSEventProperty<Boolean>

    fun nullBoolProperty(key: String, default: Boolean? = null,
                         onChange: ((value: Boolean?) -> Unit)? = null)
            : CSEventProperty<Boolean?>

    fun lateBoolProperty(key: String, onChange: ((value: Boolean) -> Unit)? = null)
            : CSEventProperty<Boolean>

    fun property(
        key: String, value: Int,
        onChange: ((value: Int) -> Unit)? = null
    ): CSEventProperty<Int>

    fun propertyNullInt(
        key: String, default: Int? = null,
        onChange: ((value: Int?) -> Unit)? = null
    ): CSEventProperty<Int?>

    fun property(
        key: String, value: Double,
        onChange: ((value: Double) -> Unit)? = null
    ): CSEventProperty<Double>

    fun property(
        key: String, value: Float,
        onChange: ((value: Float) -> Unit)? = null
    ): CSEventProperty<Float>

    fun property(
        key: String, value: Long,
        onChange: ((value: Long) -> Unit)? = null
    ): CSEventProperty<Long>

    fun <T> property(
        key: String, values: List<T>, value: T,
        onChange: ((value: T) -> Unit)? = null
    ): CSListItemValueStoreEventProperty<T>

    fun <T> property(
        key: String, values: List<T>, getDefault: () -> T,
        onChange: ((value: T) -> Unit)? = null
    ): CSListItemValueStoreEventProperty<T>

    fun <T> propertyNullListItem(
        key: String, values: List<T>, default: T?,
        onChange: ((value: T?) -> Unit)? = null
    ): CSListItemNullableStoreEventProperty<T>

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int,
        onChange: ((value: T) -> Unit)? = null
    ) = property(key, values, values[defaultIndex], onChange)

    fun <T> property(
        key: String, getValues: () -> List<T>,
        defaultIndex: Int, onChange: ((value: T) -> Unit)? = null
    ): CSListItemValueStoreEventProperty<T>

    fun <T> property(
        key: String, values: Array<T>, value: T,
        onChange: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), value, onChange)

    fun <T> property(
        key: String, values: Array<T>, valueIndex: Int,
        onChange: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), values[valueIndex], onChange)

    fun <T : CSHasId> property(
        key: String, values: Iterable<T>, value: List<T>,
        onChange: ((value: List<T>) -> Unit)? = null
    ): CSListValueStoreEventProperty<T>

    fun <T : CSHasId> property(
        key: String, values: Array<T>, value: List<T>,
        onChange: ((value: List<T>) -> Unit)? = null
    ) = property(key, values.asList(), value, onChange)
}