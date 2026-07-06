package renetik.android.store.context

import renetik.android.core.lang.CSHasId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.destruct

interface CSHasStoreContext : CSHasId {
    val store: CSStoreContext
    override val id get() = store.id

    companion object {
        // We need to be able to destroy model first so no operations
        // happen when we clear properties to clear storage.
        fun <T> T.destructClear() where T : CSHasStoreContext, T : CSHasDestruct {
            destruct()
            store.clear()
        }
    }
}