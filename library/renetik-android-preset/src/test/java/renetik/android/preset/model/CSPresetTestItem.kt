package renetik.android.preset.model

import renetik.android.preset.CSPresetItem
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestItem(
    override val id: String
) : CSPresetItem {
    override val store = CSJsonObjectStore()
    override val title: CSStoreProperty<String> = store.property("title", "title")
    override fun toString() = "${super.toString()}, id:$id"
}