package renetik.android.framework.preset.property

import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset

interface CSPresetEventProperty<T> : CSEventProperty<T>, CSHasDestroy {
    val preset: CSPreset<*, *>
    val key: String
    fun saveTo(store: CSJsonObject)
}