package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventPropertyBase
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface

abstract class CSValuePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPresetEventPropertyBase<T>(parent, preset, key, onChange), CSPresetKeyData {

    override fun loadFrom(store: CSStoreInterface): T = get(store) ?: default

    override fun load(): T = get(store) ?: default.also { value ->
        store.eventChanged.pause().use { set(store, value) }
    }
}