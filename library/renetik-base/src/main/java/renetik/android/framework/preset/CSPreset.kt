package renetik.android.framework.preset

import renetik.android.framework.CSModelBase
import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.listenOnce
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSJsonTypeValueStoreEventProperty

class CSPreset<PresetItem : CSPresetItem, PresetList : CSPresetItemList<PresetItem>>(
    parent: CSHasDestroy,
    parentStore: CSStoreInterface,
    parentId: String,
    val list: PresetList) : CSModelBase(parent), CSHasId {

    constructor(parent: CSHasDestroy, parentPreset: CSPreset<*, *>,
                parentId: String, list: PresetList)
            : this(parent, parentPreset.store.value, parentId, list)

    override val id = "$parentId preset"
    val item = parentStore.property("$id current", { list.items }, defaultIndex = 0)
    val store = CSJsonTypeValueStoreEventProperty(parentStore, "$id store",
        CSJsonObject::class, listenStoreChanged = true)

    override fun onDestroy() {
        super.onDestroy()
        store.stopListeningStoreChanged()
    }

    private val properties = mutableSetOf<CSPresetEventPropertyBase<*>>()

    init {
        if (store.value.data.isEmpty())
            reload(item.value)
        item.onChange(this::reload)
    }

    fun reload() = reload(item.value)

    fun reload(item: PresetItem) {
//        store.value.reload(item.store)
        store.value(CSJsonObject(item.store))
//        store.save()
    }

    fun <T : CSPresetEventPropertyBase<*>> add(property: T): T {
        properties.add(property)
        property.eventDestroy.listenOnce { properties.remove(property) }
        return property
    }

    fun saveAsNew(item: PresetItem) {
//        item.store.reload(store.value)
        item.save(properties) //TODO...
        list.put(item)
        this.item.value(item)
    }

    fun saveAsCurrent() {
//        item.value.store.reload(store.value)
        item.value.save(properties)//TODO...
    }

    fun delete(preset: PresetItem) {
        preset.delete()
        list.remove(preset)
        if (item.value == preset) item.value = list.items.first()
    }

    override fun toString() = "$id ${super.toString()}"
}