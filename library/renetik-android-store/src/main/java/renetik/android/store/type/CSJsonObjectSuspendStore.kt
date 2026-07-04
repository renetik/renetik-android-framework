package renetik.android.store.type

import renetik.android.event.CSSuspendEvent.Companion.suspendEvent
import renetik.android.json.obj.CSJsonObject
import renetik.android.store.CSSuspendStore

open class CSJsonObjectSuspendStore : CSSuspendStore {
    override val eventLoaded = suspendEvent<CSSuspendStore>()
    override val eventChanged = suspendEvent<CSSuspendStore>()
    open suspend fun onLoaded() = eventLoaded.fire(this)
    open suspend fun onChanged() = eventChanged.fire(this)
    override val jsonData = CSJsonObject()
    override suspend fun load(data: Map<String, Any?>) {
        jsonData.load(data)
        onLoaded()
        onChanged()
    }
}