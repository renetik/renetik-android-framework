package renetik.android.preset.model

import renetik.android.preset.CSPresetItem
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.type.CSJsonObjectStore

class NotFoundPresetItem : CSPresetItem {
    override val store: CSStore = CSJsonObjectStore()
    override val title: CSStoreProperty<String> = store.property("", "")
    override val id: String = "NotFoundPresetItemId"
}