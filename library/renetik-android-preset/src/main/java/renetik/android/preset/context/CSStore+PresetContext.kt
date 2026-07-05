package renetik.android.preset.context

import renetik.android.core.lang.CSHasId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.store.CSStore

fun CSStore.context(
    parent: CSHasDestruct? = null,
    hasId: CSHasId? = null,
    key: String? = null,
) = CustomStoreContext(parent, this, hasId, key)