package renetik.android.framework.preset

import renetik.android.framework.preset.property.CSPresetStoreEventProperty
import renetik.android.framework.CSApplication.Companion.application
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
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.store.property.CSStoreEventProperty


class CSPreset<PresetItem : CSPresetItem, PresetList : CSPresetItemList<PresetItem>>(
    parent: CSHasDestroy,
    parentPreset: CSPreset<*, *>? = null,
    val parentId: String,
    val list: PresetList) : CSModelBase(parent), CSId {

    constructor(parent: CSHasDestroy, parentId: String, list: PresetList)
            : this(parent, null, parentId, list)

    override val id = "$parentId preset"

    val current: CSEventProperty<PresetItem> = {
        parentPreset?.property(this, "$id current", list.items, defaultIndex = 0)
            ?: application.store.property("$id current", list.items, defaultIndex = 0)
    }()

    val store: CSStoreEventProperty<CSJsonObject> = {
        parentPreset?.property(this, "$id store", CSJsonObject::class, CSJsonObject())
            ?: application.store.property("$id store", CSJsonObject::class, CSJsonObject())
    }()

    init {
        if (store.value.data.isEmpty())
            reload(current.value)
        current.onChange {
            reload(it)
        }
        store.onChange {
            info(it)
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
        for (property in properties) property.save(preset.store)
        list.put(preset)
        current.value(preset)
        isModified.setFalse()
    }

    fun saveAsCurrent(): PresetItem {
        current.value.store.bulkSave().use {
            it.clear()
            for (property in current.value.properties) property.save(it)
            for (property in properties) property.save(it)
        }
        isModified.setFalse()
        return current.value
    }

    fun delete(preset: PresetItem) {
        preset.delete()
        list.delete(preset)
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
        updateIsModified(property)
        parent.eventDestroy.listenOnce {
            properties.remove(property)
            modifiedProperties.remove(property)
        }
        return property
    }

    override fun toString() = "$id ${super.toString()}"
}