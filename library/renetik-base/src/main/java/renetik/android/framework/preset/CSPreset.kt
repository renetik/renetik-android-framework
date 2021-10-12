package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.CSModelBase
import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.listenOnce
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.store.property
import renetik.android.framework.lang.CSId
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.framework.preset.property.CSPresetStoreEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty


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

    val current: CSStoreEventProperty<PresetItem> =
        parentPreset?.property(this, "$id current", list.items, default = 0)
            ?: parentStore!!.property("$id current", list.items, default = 0)

    val store: CSStoreEventProperty<CSJsonObject> =
        parentPreset?.property(this, "$id store", CSJsonObject::class)
            ?: parentStore!!.property("$id store", CSJsonObject::class)

    init {
        if (store.value.data.isEmpty())
            reload(current.value)
        current.onChange {
            reload(it)
        }
        store.onChange {
//            info(it)
        }
    }

    fun reload() = reload(current.value)

    private var isReload = false
    fun reload(item: PresetItem) {
        isReload = true
        store.value = CSJsonObject().also { it.load(item.store) }
        isReload = false
    }

    val isModified = property(false)

    private val properties = mutableSetOf<CSPresetStoreEventProperty<*>>()
    private val modifiedProperties = mutableSetOf<CSPresetStoreEventProperty<*>>()
    private fun updateIsModified(property: CSPresetStoreEventProperty<*>) {
        if (property.isModified()) {
            modifiedProperties.add(property)
        } else
            modifiedProperties.remove(property)
        isModified.isTrue = modifiedProperties.size > 0
    }

    fun saveAsNew(preset: PresetItem) {
        preset.save(properties)
//        for (property in properties) property.save(preset.store)
        list.put(preset)
        current.value(preset)
        isModified.setFalse()
    }

    fun saveAsCurrent(): PresetItem {
        current.value.save(properties)
        isModified.setFalse()
        return current.value
    }

    fun delete(preset: PresetItem) {
        preset.delete()
        list.remove(preset)
        if (current.value == preset) current.value = list.items.first()
    }

    fun <T, Property : CSPresetStoreEventProperty<T>>
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