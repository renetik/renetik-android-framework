package renetik.android.preset.property.nullable

import renetik.android.event.lifecycle.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.CSPresetItem
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.preset.model.NotFoundPresetItem
import renetik.android.preset.model.defaultCategory
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.createItem(title = "Clear Parent", category = defaultCategory)
    }

    val parentPreset: CSPreset<CSPresetItem, CSPresetTestPresetItemList> = CSPreset(
        parent = this, parentStore = store, "parentClass parent",
        presetList, notFoundItem = { NotFoundPresetItem() },
    )
    val property1 = parentPreset.nullIntProperty(this, "property1", 1)
    val property2 = parentPreset.nullIntProperty(this, "property2", 2)
}
