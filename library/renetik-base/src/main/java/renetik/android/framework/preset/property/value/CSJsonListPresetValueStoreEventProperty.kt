package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.property.value.CSJsonListValueStoreEventProperty
import kotlin.reflect.KClass

class CSJsonListPresetValueStoreEventProperty<T : CSJsonObject>(
    preset: CSPreset<*, *>,
    key: String,
    type: KClass<T>,
    default: List<T> = emptyList(),
    onChange: ((value: List<T>) -> Unit)?)
    : CSPresetValueStoreEventProperty<List<T>>(preset, key, default) {
    override val property = CSJsonListValueStoreEventProperty(store, key, type, default, onChange)
}