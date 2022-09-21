package renetik.android.network.ping

import renetik.android.network.operation.CSOperation
import renetik.android.network.process.CSProcessBase
import renetik.android.network.process.CSServerWithPing

open class CSPingOperation<Data : Any>(
    server: CSServerWithPing,
    onPingDone: CSOperation<*>.() -> CSProcessBase<Data>)
    : CSOperation<Data>({ CSPingMultiProcess(this, server) { onPingDone() } })