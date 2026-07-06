package renetik.android.store.property

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.parent
import renetik.android.event.property.CSProperty
import renetik.android.store.CSStore
import renetik.android.store.property.value.CSBooleanValueStoreProperty

object CSStoreAction {
    var actionsDisabled = false
    fun CSHasDestruct.action(id: String): CSProperty<Boolean> =
        object : CSBooleanValueStoreProperty(CSStore.fileStore, id, false) {
            override fun get(store: CSStore) =
                if (!actionsDisabled) store.getBoolean(key) else false
        }.parent(this)
}