package renetik.android.framework.preset.property.nullable

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.preset.property.CSPresetEventPropertyBase
import renetik.android.framework.store.CSStoreInterface

abstract class CSNullablePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?)
    : CSPresetEventPropertyBase<T?>(parent, preset, key, onChange), CSPresetEventProperty<T?> {

    override fun load(store: CSStoreInterface): T? =
        if (store.has(key)) get(store) else default.also { set(store, it) }
}