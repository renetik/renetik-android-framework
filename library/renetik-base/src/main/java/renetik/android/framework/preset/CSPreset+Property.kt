package renetik.android.framework.preset

import renetik.android.framework.preset.property.nullable.CSFloatPresetNullableStoreEventProperty
import renetik.android.framework.preset.property.nullable.CSIntPresetNullableStoreEventProperty
import renetik.android.framework.preset.property.nullable.CSListItemPresetNullableStoreEventProperty
import renetik.android.framework.preset.property.value.*
import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.CSId
import kotlin.reflect.KClass

fun CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)? = null
) = setupProperty(parent, CSBooleanPresetValueStoreEventProperty(this, key, default, onChange))

fun CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null
) = setupProperty(parent, CSIntPresetValueStoreEventProperty(this, key, default, onChange))

fun CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Float,
    onChange: ((value: Float) -> Unit)? = null
) = setupProperty(parent, CSFloatPresetValueStoreEventProperty(this, key, default, onChange))

fun CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, default: Double,
    onChange: ((value: Double) -> Unit)? = null
) = setupProperty(parent, CSDoublePresetValueStoreEventProperty(this, key, default, onChange))

fun <T> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSListItemPresetValueStoreEventProperty(this, key, values, default, onChange))

fun <T> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSListItemPresetValueStoreEventProperty(this, key, values, getDefault, onChange))

fun <T> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values, values[defaultIndex], onChange)

fun <T> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), values[defaultIndex], onChange)

fun <T : CSId> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = setupProperty(parent, CSListPresetValueStoreEventProperty(this, key, values, default, onChange))

fun <T : CSId> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T : CSJsonObject> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, listType: KClass<T>,
    default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) = setupProperty(parent,
    CSJsonListPresetValueStoreEventProperty(this, key, listType, default, onApply))

fun <T : CSJsonObject> CSPreset<*,*>.property(
    parent: CSEventOwnerHasDestroy, key: String, type: KClass<T>,
    default: T,
    onApply: ((value: T) -> Unit)? = null
) = setupProperty(parent,
    CSJsonTypePresetValueStoreEventProperty(this, key, type, default, onApply))

fun CSPreset<*,*>.propertyNullInt(
    parent: CSEventOwnerHasDestroy, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null
) = setupProperty(parent, CSIntPresetNullableStoreEventProperty(this, key, default, onChange))

fun CSPreset<*,*>.propertyNullFloat(
    parent: CSEventOwnerHasDestroy, key: String, default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null
) = setupProperty(parent, CSFloatPresetNullableStoreEventProperty(this, key, default, onChange))

fun <T> CSPreset<*,*>.propertyNullListItem(
    parent: CSEventOwnerHasDestroy, key: String, values: List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = setupProperty(parent,
    CSListItemPresetNullableStoreEventProperty(this, key, values, default, onChange))

fun <T> CSPreset<*,*>.propertyNullArrayItem(
    parent: CSEventOwnerHasDestroy, key: String, values: Array<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = propertyNullListItem(parent, key, values.asList(), default, onChange)