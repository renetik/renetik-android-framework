package renetik.android.network.ping

import renetik.android.network.operation.CSOperation
import renetik.android.network.process.CSProcess
import renetik.android.network.CSServerWithPing

open class CSPingOperation<Data : Any>(
    server: CSServerWithPing,
    onPingDone: CSOperation<*>.() -> CSProcess<Data>)
    : CSOperation<Data>({ CSPingMultiProcess(this, server) { onPingDone() } })