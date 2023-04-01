package renetik.android.network.ping

import renetik.android.network.operation.CSOperation
import renetik.android.network.process.CSConcurrentProcess
import renetik.android.network.CSServerWithPing

open class CSPingConcurrentOperation<T : Any>(
    server: CSServerWithPing,
    onPingDone: CSOperation<*>.(CSConcurrentProcess<T>) -> Unit)
    : CSPingOperation<List<T>>(server, {
    CSConcurrentProcess<T>(this).apply { onPingDone(this@apply) }
})