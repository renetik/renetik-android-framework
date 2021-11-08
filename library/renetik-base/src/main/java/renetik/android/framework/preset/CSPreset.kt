package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.CSModelBase
import renetik.android.framework.event.*
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

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

class CSPresetStoreItem<PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>>(
    override val preset: CSPreset<PresetItem, PresetList>,
    val parentStore: CSStoreInterface
) : CSModelBase(preset), CSEventProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStoreInterface) = store.set(key, value.toId())

    val values get() = preset.list.items

    var _value: PresetItem = loadValue()

    private fun loadValue(): PresetItem {
        return parentStore.getValue(key, values) ?: values[0]
    }

    private val eventChange = event<PresetItem>()

    val parentStoreChanged = register(parentStore.eventChanged.listen {
        val newValue = loadValue()
        if (_value == newValue) return@listen
        _value = newValue
        eventChange.fire(newValue)
    })

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        parentStoreChanged.pause().use {
            saveTo(parentStore)
            if (fire) eventChange.fire(newValue)
            preset.reload(newValue)
        }
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()}, value:$value"
}

class CSPresetStore(
    override val preset: CSPreset<*, *>,
    val parentStore: CSStoreInterface) : CSJsonObject(), CSPresetKeyData {

    override val key = "${preset.id} store"
    override fun saveTo(store: CSStoreInterface) = store.set(key, data)
    override val eventDestroy get() = preset.eventDestroy
    override fun onDestroy() = preset.onDestroy()

    private val parentStoreEventChanged = preset.register(parentStore.eventChanged.listen {
        onParentStoreChanged(it.getMap(key) ?: emptyMap<String, Any>())
    })

    private fun onParentStoreChanged(data: Map<String, *>) {
        if (this.data == data) return
        parentStoreEventChanged.pause().use {
            this.data.clear()
            this.data.putAll(data)
            eventChanged.fire(this)
        }
    }

    init {
        parentStore.getMap(key)?.let { data -> load(data) }
    }

    override fun onChanged() {
        parentStoreEventChanged.pause().use {
            saveTo(parentStore)
            super.onChanged()
        }
    }
}