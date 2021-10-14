package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.CSId
import renetik.android.framework.preset.property.nullable.CSFloatNullablePresetEventProperty
import renetik.android.framework.preset.property.nullable.CSIntNullablePresetEventProperty
import renetik.android.framework.preset.property.nullable.CSListItemNullablePresetEventProperty
import renetik.android.framework.preset.property.value.*
import kotlin.reflect.KClass

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: String,
    onChange: ((value: String) -> Unit)? = null
) = setupProperty(parent, CSStringValuePresetEventProperty(this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)? = null
) = setupProperty(parent, CSBooleanValuePresetEventProperty(this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null
) = setupProperty(parent, CSIntValuePresetEventProperty(this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Float,
    onChange: ((value: Float) -> Unit)? = null
) = setupProperty(parent, CSFloatValuePresetEventProperty(this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Double,
    onChange: ((value: Double) -> Unit)? = null
) = setupProperty(parent, CSDoubleValuePresetEventProperty(this, key, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSListItemValuePresetEventProperty(this, key, values, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSListItemValuePresetEventProperty(this, key, values, getDefault, onChange))

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

fun <T : CSId> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = setupProperty(parent, CSListValuePresetEventProperty(this, key, values, default, onChange))

fun <T : CSId> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T : CSJsonObject> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, listType: KClass<T>,
    default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) = setupProperty(parent,
    CSJsonListValuePresetEventProperty(this, key, listType, default, onApply))

fun <T : CSJsonObject> CSPreset<*, *>.property(
    parent: CSEventOwnerHasDestroy, key: String, type: KClass<T>,
    onApply: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSJsonTypeValuePresetEventProperty(this, key, type, onApply))

fun CSPreset<*, *>.propertyNullInt(
    parent: CSEventOwnerHasDestroy, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null
) = setupProperty(parent, CSIntNullablePresetEventProperty(this, key, default, onChange))

fun CSPreset<*, *>.propertyNullFloat(
    parent: CSEventOwnerHasDestroy, key: String, default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null
) = setupProperty(parent, CSFloatNullablePresetEventProperty(this, key, default, onChange))

fun <T> CSPreset<*, *>.propertyNullListItem(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = setupProperty(parent,
    CSListItemNullablePresetEventProperty(this, key, values, default, onChange))

fun <T> CSPreset<*, *>.propertyNullArrayItem(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = propertyNullListItem(parent, key, values.asList(), default, onChange)