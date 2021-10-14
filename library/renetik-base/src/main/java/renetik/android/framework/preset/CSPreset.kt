package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.CSModelBase
import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.listenOnce
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.store.property
import renetik.android.framework.lang.CSId
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
    val list: PresetList) : CSModelBase(parent), CSId {

    constructor(parent: CSHasDestroy, parentStore: CSStoreInterface,
                parentId: String, list: PresetList)
            : this(parent, parentPreset = null, parentStore = parentStore,
        parentId = parentId, list = list)

    constructor(parent: CSHasDestroy, parentPreset: CSPreset<*, *>,
                parentId: String, list: PresetList)
            : this(parent, parentPreset = parentPreset, parentStore = null,
        parentId = parentId, list = list)

    override val id = "$parentId preset"

    val current: CSEventProperty<PresetItem> =
        parentPreset?.property(this, "$id current", { list.items }, defaultIndex = 0)
            ?: parentStore!!.property("$id current", { list.items }, defaultIndex = 0)

    val store: CSEventProperty<CSJsonObject> =
        parentPreset?.let {
            it.setupProperty(this, CSStoreJsonTypePresetValueStoreEventProperty(this, it))
        } ?: parentStore!!.property("$id store", CSJsonObject::class)

    private val properties = mutableSetOf<CSPresetEventProperty<*>>()

    init {
        if (store.value.data.isEmpty()) {
            reload(current.value)
        }
        current.onChange {
            reload(it)
        }
    }

    fun reload() = reload(current.value)

    var isReload = false
    fun reload(item: PresetItem) {
        isReload = true
        val storeValue = CSJsonObject(item.store)
        properties.forEach { it.reload(storeValue) }
        store.value = CSJsonObject(item.store)
        isReload = false
    }

    val isModified = property(false)
    private val modifiedProperties = mutableSetOf<CSPresetEventProperty<*>>()
    private fun updateIsModified(property: CSPresetEventProperty<*>) {
        if (property.isModified())
            modifiedProperties.add(property)
        else
            modifiedProperties.remove(property)
        isModified.isTrue = modifiedProperties.size > 0
    }

    fun saveAsNew(preset: PresetItem) {
        preset.save(properties)
        list.put(preset)
        current.value(preset)
    }

    fun saveAsCurrent() {
        current.value.save(properties)
        modifiedProperties.clear()
        isModified.setFalse()
    }

    fun delete(preset: PresetItem) {
        preset.delete()
        list.remove(preset)
        if (current.value == preset) current.value = list.items.first()
    }

    fun <T, Property : CSPresetEventProperty<T>>
            setupProperty(parent: CSEventOwnerHasDestroy, property: Property): Property {
        properties.add(property)
        property.onChange {
            updateIsModified(property)
        }
        current.onChange {
            updateIsModified(property)
        }
        store.onChange {
            updateIsModified(property)
        }
        updateIsModified(property)
        parent.eventDestroy.listenOnce {
            properties.remove(property)
            modifiedProperties.remove(property)
        }
        return property
    }

    override fun toString() = "$id ${super.toString()}"
}