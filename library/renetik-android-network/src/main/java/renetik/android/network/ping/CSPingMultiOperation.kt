package renetik.android.network.ping

import renetik.android.network.operation.CSOperation
import renetik.android.event.process.CSMultiProcessBase
import renetik.android.network.CSServerWithPing

open class CSPingMultiOperation<Data : Any>(
    server: CSServerWithPing,
    items: Data,
    onPingDone: CSOperation<*>.(CSMultiProcessBase<Data>) -> Unit)
    : CSOperation<Data>({
    CSPingMultiProcess(this, server, items) {
        onPingDone(this@CSPingMultiProcess)
    }
})