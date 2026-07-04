package renetik.android.preset.model

import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.extensions.operation

const val defaultCategory = "default"
const val userCategory = "user"

fun <T : CSPreset<*, *>> T.manageItems() = apply {
    eventSave.listen { item ->
        item.store.operation {
            it.clear()
            for (property: CSPresetKeyData in properties)
                property.saveTo(it)
            for (preset: CSPreset<*, *> in presets)
                preset.store.saveTo(it)
        }
    }
}