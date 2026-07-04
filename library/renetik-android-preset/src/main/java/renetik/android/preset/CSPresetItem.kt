package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

interface CSPresetItem : CSHasId {
    val store: CSStore
    val title: CSStoreProperty<String>
}