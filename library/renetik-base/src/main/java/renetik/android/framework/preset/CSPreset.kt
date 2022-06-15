package renetik.android.framework.preset

import renetik.android.framework.base.CSBase
import renetik.android.framework.event.CSAction.Companion.action
import renetik.android.framework.event.listenOnce
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.event.register
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.lang.property.connect
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.store.CSStore
import renetik.kotlin.unexpected

class CSPreset<PresetItem : CSPresetItem, PresetList : CSPresetItemList<PresetItem>>(
    parent: CSEventOwnerHasDestroy,
    parentStore: CSStore,
    key: String,
    val list: PresetList,
    val getDefault: () -> PresetItem = { list.items[0] })
    : CSBase(parent), CSHasId {

    override val id = "$key preset"
    val actionEdit = action("$id actionEdit")
    val isFollowStore = property(true)
    val item = CSPresetStoreItemProperty(this, parentStore, getDefault)
    val store = CSPresetStore(this, parentStore)
    private val dataList = mutableListOf<CSPresetKeyData>()

    constructor (
        parent: CSEventOwnerHasDestroy, parentStore: CSStore,
        key: String, list: PresetList, defaultItemId: String)
            : this(parent, parentStore, key, list,
        { list.defaultList.let { list -> list.find { it.id == defaultItemId } ?: list[0] } })

    @Deprecated("Used just in test now")
    constructor(parent: CSEventOwnerHasDestroy, parentPreset: CSPreset<*, *>,
                parentId: String, list: PresetList)
            : this(parent, parentPreset.store, parentId, list) {
        parentPreset.add(item)
        parentPreset.add(store)
    }

    constructor(parent: CSHasPreset, key: String,
                list: PresetList, getDefault: () -> PresetItem = { list.items[0] })
            : this(parent, parent.preset.store, "${parent.presetId} $key", list, getDefault) {
        parent.preset.add(item)
        parent.preset.add(store)
    }

    init {
        if (store.data.isEmpty()) reload(item.value)
    }

    fun reload() = reload(item.value)

    fun reload(item: PresetItem) = store.reload(item.store)

    fun <T : CSPresetKeyData> add(property: T): T {
        if (dataList.contains(property)) unexpected()
        dataList.add(property)
        property.eventDestroy.listenOnce { dataList.remove(property) }
        return property
    }

    fun saveAsNew(item: PresetItem) {
        item.save(dataList)
        list.add(item)
        this.item.value(item)
    }

    fun saveAsCurrent() =
        item.value.save(dataList)

//    fun delete(preset: PresetItem) {
//        preset.delete()
//        list.remove(preset)
//        if (item.value == preset) item.value = list.items.first()
//    }

    override fun toString() = "$id ${super.toString()}"
}

fun Preset.followStoreIf(property: CSEventProperty<Boolean>) {
    register(isFollowStore.connect(property))
}