package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.CSModelBase
import renetik.android.framework.event.listenOnce
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface

class CSPreset<PresetItem : CSPresetItem, PresetList : CSPresetItemList<PresetItem>>(
    parent: CSEventOwnerHasDestroy,
    parentStore: CSStoreInterface,
    parentId: String,
    val list: PresetList) : CSModelBase(parent), CSHasId {

    override val id = "$parentId preset"
    val item = CSPresetStoreItem(this, parentStore)
    val store = CSPresetStore(this, parentStore)
    private val properties = mutableSetOf<CSPresetKeyData>()

    constructor(parent: CSEventOwnerHasDestroy, parentPreset: CSPreset<*, *>,
                parentId: String, list: PresetList)
            : this(parent, parentPreset.store, parentId, list) {
        parentPreset.add(item)
        parentPreset.add(store)
    }

    init {
        if (store.data.isEmpty()) reload(item.value)
    }

    fun reload() = reload(item.value)

    fun reload(item: PresetItem) {
        store.reload(item.store)
    }

    fun <T : CSPresetKeyData> add(property: T): T {
        properties.add(property)
        property.eventDestroy.listenOnce { properties.remove(property) }
        return property
    }

    fun saveAsNew(item: PresetItem) {
        item.save(properties)
        list.put(item)
        this.item.value(item)
    }

    fun saveAsCurrent() = item.value.save(properties)

    fun delete(preset: PresetItem) {
        preset.delete()
        list.remove(preset)
        if (item.value == preset) item.value = list.items.first()
    }

    override fun toString() = "$id ${super.toString()}"
}