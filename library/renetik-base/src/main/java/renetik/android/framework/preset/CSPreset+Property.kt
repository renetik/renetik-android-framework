package renetik.android.framework.preset

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.json.CSJsonObject
import renetik.android.core.lang.CSHasId
import renetik.android.framework.preset.property.nullable.CSFloatNullablePresetEventProperty
import renetik.android.framework.preset.property.nullable.CSIntNullablePresetEventProperty
import renetik.android.framework.preset.property.nullable.CSListItemNullablePresetEventProperty
import renetik.android.framework.preset.property.nullable.CSStringNullablePresetEventProperty
import renetik.android.framework.preset.property.value.*
import kotlin.reflect.KClass

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: String,
    onChange: ((value: String) -> Unit)? = null
) = add(CSStringValuePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)? = null
) = add(CSBooleanValuePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null
) = add(CSIntValuePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Float,
    onChange: ((value: Float) -> Unit)? = null
) = add(CSFloatValuePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Double,
    onChange: ((value: Double) -> Unit)? = null
) = add(CSDoubleValuePresetEventProperty(parent, this, key, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetEventProperty(parent, this, key, values, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetEventProperty(parent, this, key,
    { values }, getDefault, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, getValues: () -> Collection<T>,
    getDefault: () -> T, onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetEventProperty(parent, this, key,
    getValues, getDefault, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, getValues: () -> List<T>,
    defaultIndex: Int, onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetEventProperty(parent, this, key, getValues,
    { getValues()[defaultIndex] }, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values, values[defaultIndex], onChange)

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), values[defaultIndex], onChange)

fun <T : CSHasId> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = add(CSListValuePresetEventProperty(parent, this, key, values, default, onChange))

fun <T : CSHasId> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T : CSJsonObject> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, listType: KClass<T>,
    default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) = add(CSJsonListValuePresetEventProperty(parent, this,
    key, listType, default, onApply))

fun <T : CSJsonObject> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, type: KClass<T>,
    onApply: ((value: T) -> Unit)? = null
) = add(CSJsonTypeValuePresetEventProperty(parent, this, key, type, onApply))

fun CSPreset<*, *>.propertyNullString(
    parent: CSEventOwnerHasDestroy, key: String, default: String? = null,
    onChange: ((value: String?) -> Unit)? = null
) = add(CSStringNullablePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.propertyNullInt(
    parent: CSEventOwnerHasDestroy, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null
) = add(CSIntNullablePresetEventProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.propertyNullFloat(
    parent: CSEventOwnerHasDestroy, key: String, default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null
) = add(CSFloatNullablePresetEventProperty(parent, this, key, default, onChange))

fun <T> CSPreset<*, *>.propertyNullListItem(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetEventProperty(parent, this,
    key, values, default, onChange))

fun <T> CSPreset<*, *>.propertyNullListItem(
    parent: CSEventOwnerHasDestroy, key: String, getValues: () -> List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetEventProperty(parent, this,
    key, getValues, { default }, onChange))

fun <T> CSPreset<*, *>.propertyNullArrayItem(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = propertyNullListItem(parent, key, values.asList(), default, onChange)