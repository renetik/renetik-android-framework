package renetik.android.preset.context

import renetik.android.core.lang.CSHasId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.store.CSStore.Companion.fileStore
import renetik.android.store.CSStore.Companion.runtimeStore
import renetik.android.store.context.CSStoreContext
import renetik.android.store.type.CSJsonObjectStore

fun CSStoreContext.Companion.empty() =
    CustomStoreContext(store = CSJsonObjectStore(), key = "")

fun <Parent> AppStoreContext(parent: Parent): CustomStoreContext
        where Parent : CSHasDestruct, Parent : CSHasId =
    CustomStoreContext(parent, fileStore, key = parent.id)

fun AppStoreContext(
    parent: CSHasDestruct, hasId: CSHasId? = null, key: String? = null,
): CustomStoreContext = CustomStoreContext(parent, fileStore, hasId, key)

fun RuntimeStoreContext(
    parent: CSHasDestruct, hasId: CSHasId? = null, key: String? = null,
): CustomStoreContext = CustomStoreContext(parent, runtimeStore, hasId, key)