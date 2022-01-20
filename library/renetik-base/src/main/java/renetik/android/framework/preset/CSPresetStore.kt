package renetik.android.framework.preset

import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.register
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.data.reload
import renetik.android.framework.lang.property.isFalse
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface

class CSPresetStore(
    override val preset: CSPreset<*, *>,
    val parentStore: CSStoreInterface) : CSJsonObject(), CSPresetKeyData {

    override val key = "${preset.id} store"
    override fun saveTo(store: CSStoreInterface) = store.set(key, data)
    override val eventDestroy get() = preset.eventDestroy
    override fun onDestroy() = preset.onDestroy()

    private val parentStoreEventChanged = preset.register(parentStore.eventChanged.listen {
        if (preset.isFollowStore.isFalse) saveTo(parentStore)
        else onParentStoreChanged(it.getMap(key) ?: emptyMap<String, Any>())
    })

    private fun onParentStoreChanged(data: Map<String, *>) {
        if (this.data == data) return
        parentStoreEventChanged.pause().use {
            if (data.isEmpty()) reload(preset.item.value.store) else reload(data)
        }
    }

    init {
        parentStore.getMap(key)?.let { data -> load(data) }
    }

    override fun onChanged() {
        parentStoreEventChanged.pause().use {
            super.onChanged()
            saveTo(parentStore)
        }
    }

    val eventReload = event<CSPresetItem>()
    val eventAfterReload = event<CSPresetItem>()

    fun reload(item: CSPresetItem) {
        eventReload.fire(item)
        super.reload(item.store)
        eventAfterReload.fire(item)
    }

    override fun equals(other: Any?) =
        (other as? CSPresetStore)?.let { it.key == key && super.equals(other) }
            ?: super.equals(other)

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + super.hashCode()
        return result
    }
}