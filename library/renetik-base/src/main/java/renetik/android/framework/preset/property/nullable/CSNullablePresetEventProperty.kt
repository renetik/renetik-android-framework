package renetik.android.framework.preset.property.nullable

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventPropertyBase
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.preset.property.store
import renetik.android.framework.store.CSStore

abstract class CSNullablePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?)
    : CSPresetEventPropertyBase<T?>(parent, preset, key, onChange), CSPresetKeyData {

    override fun loadFrom(store: CSStore): T? =
        if (store.has(key)) get(store) else default

    override fun load(): T? =
        if (store.has(key)) get(store) else default?.also { value ->
            store.eventChanged.pause().use { set(store, value) }
        }
}