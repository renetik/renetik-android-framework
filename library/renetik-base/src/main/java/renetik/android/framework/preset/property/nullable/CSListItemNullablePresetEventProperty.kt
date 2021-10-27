package renetik.android.framework.preset.property.nullable

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.lang.property.CSListValuesEventProperty
import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemNullablePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val values: List<T>,
    override val default: T?,
    onChange: ((value: T?) -> Unit)?)
    : CSNullablePresetEventProperty<T>(parent, preset, key, onChange),
    CSListValuesEventProperty<T?> {
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T?) {
        store.set(key, value.toId())
    }
}