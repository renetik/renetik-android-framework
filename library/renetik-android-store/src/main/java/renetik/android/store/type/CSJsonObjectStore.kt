package renetik.android.store.type

import renetik.android.event.CSEvent.Companion.event
import renetik.android.json.obj.CSJsonObject
import renetik.android.store.CSStore

open class CSJsonObjectStore : CSJsonObject(), CSStore {

    companion object {
        fun CSJsonObjectStore(data: Map<String, Any?>) =
            CSJsonObjectStore().apply { load(data) }
    }

    override val eventLoaded = event<CSStore>()
    override val eventChanged = event<CSStore>()
    open fun onLoaded() = eventLoaded.fire(this)
    open fun onChanged() = eventChanged.fire(this)
    var isOperation = false
    private var isLoadWhenOperation = false
    private var isChangeWhenOperation = false

    override fun onLoad() {
        if (!isOperation) onLoaded()
        else isLoadWhenOperation = true
    }

    override fun onChange() {
        if (!isOperation) onChanged()
        else isChangeWhenOperation = true
    }

    override fun startOperation(): Boolean {
        if (!isOperation) {
            isOperation = true
            isLoadWhenOperation = false
            isChangeWhenOperation = false
            return true
        }
        return false
    }

    override fun stopOperation() {
        isOperation = false
        if (isLoadWhenOperation) onLoaded()
        isLoadWhenOperation = false
        if (isChangeWhenOperation) onChanged()
        isChangeWhenOperation = false
    }
}

