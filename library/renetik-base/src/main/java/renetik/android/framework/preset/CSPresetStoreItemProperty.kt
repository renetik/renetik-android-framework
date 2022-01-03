package renetik.android.framework.preset

import renetik.android.framework.CSModelBase
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.register
import renetik.android.framework.lang.property.isFalse
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSPresetStoreItemProperty<PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>>(
    override val preset: CSPreset<PresetItem, PresetList>,
    val parentStore: CSStoreInterface
) : CSModelBase(preset), CSEventProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStoreInterface) = store.set(key, value.toId())
    private var _value: PresetItem = loadValue()
    private val values: List<PresetItem> get() = preset.list.items
    private fun loadValue(): PresetItem =
        parentStore.getValue(key, values) ?: values[0]

    private val eventChange = event<PresetItem>()

    val parentStoreChanged = register(parentStore.eventChanged.listen { onParentStoreChange() })
    private fun onParentStoreChange() {
        if (preset.isFollowStore.isFalse)
            parentStoreChangedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = loadValue()
            if (_value == newValue) return
            _value = newValue
            //TODO Experimental Change !!! This is maybe useless also in property base
            parentStoreChanged.pause().use { eventChange.fire(newValue) }
        }
    }

    //TODO Experimental Change !!!
    private fun parentStoreChangedIsFollowStoreFalseSaveToParentStore() =
        parentStore.eventChanged.pause().use { saveTo(parentStore) }

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        parentStoreChanged.pause().use {
            if (fire) eventChange.fire(newValue)
            preset.reload(newValue)
            saveTo(parentStore)
        }
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
}