package renetik.android.client.request


interface CSServerWithPing {
    fun ping(): CSProcessBase<CSServerMapData>
}

open class CSPingConcurrentRequest<T:Any>(server: CSServerWithPing, onPingDone: CSOperation<*>.(CSConcurrentProcess<T>) -> Unit)
    : CSPingRequest<List<T>>(server, { CSConcurrentProcess<T>().apply { onPingDone(this@apply) } })

open class CSPingRequest<Data : Any>(server: CSServerWithPing, onPingDone: CSOperation<*>.() -> CSProcessBase<Data>)
    : CSOperation<Data>({ CSPingMultiProcess(server) { onPingDone() } })

open class CSPingMultiRequest<Data : Any>(server: CSServerWithPing, items: Data,
                                          onPingDone: CSOperation<*>.(CSMultiProcess<Data>) -> Unit)
    : CSOperation<Data>({ CSPingMultiProcess(server, items) { onPingDone(this@CSPingMultiProcess) } })

open class CSPingMultiProcess<Data : Any> : CSMultiProcess<Data> {

    constructor(server: CSServerWithPing, onPingDone: () -> CSProcessBase<Data>)
            : this(server, null, { addLast(onPingDone()) })

    constructor(server: CSServerWithPing, data: Data?, onPingDone: CSMultiProcess<Data>.() -> Unit)
            : super(data) {
        addedProcess = server.ping().onSuccess(onPingSuccess(onPingDone))
                .onFailed { addedProcess = server.ping().onDone(onPingSuccess(onPingDone)) }
    }

    private fun onPingSuccess(onPingDone: CSMultiProcess<Data>.() -> Unit)
            : (CSProcessBase<CSServerMapData>) -> Unit = { if (!it.isCanceled) onPingDone(this) }
}