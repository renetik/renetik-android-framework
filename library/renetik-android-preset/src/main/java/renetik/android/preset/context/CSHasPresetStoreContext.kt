package renetik.android.preset.context

import renetik.android.store.context.CSHasStoreContext

interface CSHasPresetStoreContext : CSHasStoreContext {
    override val store: PresetStoreContext
    override val id get() = store.id
}