package renetik.android.network.ping

import renetik.android.network.operation.CSOperation
import renetik.android.network.process.CSMultiProcessBase
import renetik.android.network.process.CSServerWithPing

open class CSPingMultiOperation<Data : Any>(
    server: CSServerWithPing,
    items: Data,
    onPingDone: CSOperation<*>.(CSMultiProcessBase<Data>) -> Unit)
    : CSOperation<Data>({
    CSPingMultiProcess(this, server, items) {
        onPingDone(this@CSPingMultiProcess)
    }
})