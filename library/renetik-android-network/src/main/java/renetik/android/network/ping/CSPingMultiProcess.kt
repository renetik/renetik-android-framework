package renetik.android.network.ping

import renetik.android.event.common.CSHasDestruct
import renetik.android.network.data.CSServerMapData
import renetik.android.network.process.CSMultiProcessBase
import renetik.android.network.process.CSProcess
import renetik.android.network.process.CSServerWithPing

open class CSPingMultiProcess<Data : Any> : CSMultiProcessBase<Data> {

    constructor(parent: CSHasDestruct, server: CSServerWithPing,
                onPingDone: () -> CSProcess<Data>)
            : this(parent, server, null, { addLast(onPingDone()) })

    constructor(parent: CSHasDestruct, server: CSServerWithPing, data: Data?,
                onPingDone: CSMultiProcessBase<Data>.() -> Unit)
            : super(parent, data) {
        addedProcess = server.ping().onSuccess(onPingSuccess(onPingDone))
            .onFailed { addedProcess = server.ping().onDone(onPingSuccess(onPingDone)) }
    }

    private fun onPingSuccess(onPingDone: CSMultiProcessBase<Data>.() -> Unit)
            : (CSProcess<CSServerMapData>) -> Unit = { if (!it.isCanceled) onPingDone(this) }
}