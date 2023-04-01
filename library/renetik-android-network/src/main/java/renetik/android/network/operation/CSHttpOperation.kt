package renetik.android.network.operation

import renetik.android.event.process.CSProcess

open class CSHttpOperation<Data : Any>(
    createProcess: CSOperation<Data>.() -> CSProcess<Data>
)
    : CSOperation<Data>(createProcess) {
    var isRefresh = false
    var isCached = true
    var expireMinutes: Int? = 1
    var isJustUseCache = false
}