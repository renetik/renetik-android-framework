package renetik.android.framework.preset

import renetik.android.framework.CSModelBase
import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.event
import renetik.android.framework.event.listenOnce
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.store.property
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface

class CSPreset<PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>> private constructor(
    parent: CSHasDestroy,
    parentPreset: CSPreset<*, *>? = null,
    parentStore: CSStoreInterface? = null,
    val parentId: String,
    val list: PresetList) : CSModelBase(parent), CSHasId {

    constructor(parent: CSHasDestroy, parentStore: CSStoreInterface,
                parentId: String, list: PresetList)
            : this(parent, parentPreset = null, parentStore = parentStore,
        parentId = parentId, list = list)

    constructor(parent: CSHasDestroy, parentPreset: CSPreset<*, *>,
                parentId: String, list: PresetList)
            : this(parent, parentPreset = parentPreset, parentStore = null,
        parentId = parentId, list = list)

    override val id = "$parentId preset"

    val item: CSEventProperty<PresetItem> =
        parentPreset?.property(this, "$id current", { list.items }, defaultIndex = 0)
            ?: parentStore!!.property("$id current", { list.items }, defaultIndex = 0)

    val store: CSEventProperty<CSJsonObject> =
        parentPreset?.let { it.add(CSStoreJsonTypePresetValueStoreEventProperty(this, it)) }
            ?: parentStore!!.property("$id store", CSJsonObject::class)

    var isReload = false
    val eventReload = event<CSJsonObject>()

    init {
        if (store.value.data.isEmpty()) {
            reload(item.value)
        }
        item.onChange {
            reload(it)
        }
    }

    fun reload() = reload(item.value)

    fun reload(item: PresetItem) {
        isReload = true
        val storeValue = CSJsonObject(item.store)
        eventReload.fire(storeValue)
        store.value = storeValue
        isReload = false
    }

    val isModified = property(false)

    private val properties = mutableSetOf<CSPresetEventProperty<*>>()
    private val modifiedProperties = mutableSetOf<CSPresetEventProperty<*>>()
    private fun updateIsModified(property: CSPresetEventProperty<*>) {
        if (property.isModified.isTrue)
            modifiedProperties.add(property)
        else
            modifiedProperties.remove(property)
        isModified.isTrue = modifiedProperties.size > 0
    }

    fun <T : CSPresetEventProperty<*>> add(property: T): T {
        properties.add(property)
        property.isModified.onChange { updateIsModified(property) }
        property.eventDestroy.listenOnce {
            properties.remove(property)
            modifiedProperties.remove(property)
        }
        return property
    }

    fun saveAsNew(preset: PresetItem) {
        preset.save(properties)
        list.put(preset)
        item.value(preset)
    }

    fun saveAsCurrent() {
        item.value.save(properties)
        modifiedProperties.clear()
        isModified.setFalse()
    }

    fun delete(preset: PresetItem) {
        preset.delete()
        list.remove(preset)
        if (item.value == preset) item.value = list.items.first()
    }

    override fun toString() = "$id ${super.toString()}"
}