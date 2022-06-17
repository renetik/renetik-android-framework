package renetik.android.preset

import renetik.android.core.lang.property.isFalse
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.listen
import renetik.android.event.register
import renetik.android.event.registration.pause
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.json.CSStoreJsonObject
import renetik.android.store.json.reload

class CSPresetStore(
    override val preset: CSPreset<*, *>,
    val parentStore: CSStore) : CSStoreJsonObject(), CSPresetKeyData {

    override val key = "${preset.id} store"
    override fun saveTo(store: CSStore) = store.set(key, data)
    override val eventDestroy get() = preset.eventDestroy
    override fun onDestroy() = preset.onDestroy()

    private val parentStoreEventChanged = preset.register(parentStore.eventChanged.listen {
        if (preset.isFollowStore.isFalse) saveTo(parentStore)
        else onParentStoreChanged(it.getMap(key) ?: emptyMap<String, Any>())
    })

    private fun onParentStoreChanged(data: Map<String, *>) {
        if (this.data == data) return
        parentStoreEventChanged.pause().use {
            if (data.isEmpty()) super.reload(preset.item.value.store)
            else reload(data)
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

    val eventReload = event()
    val eventAfterReload = event()

    override fun reload(store: CSStore) {
        eventReload.fire()
        super.reload(store)
        eventAfterReload.fire()
    }

    override fun equals(other: Any?) =
        (other as? CSPresetStore)?.let { it.key == key && super.equals(other) }
            ?: super.equals(other)

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + super.hashCode()
        return result
    }

    fun clone() = CSPresetStore(preset, parentStore).also { it.load(this) }
}