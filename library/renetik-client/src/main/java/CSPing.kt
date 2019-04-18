package renetik.android.client.request


interface CSServerWithPing {
    fun ping(): CSProcess<CSServerData>
}

open class CSPingConcurrentRequest(server: CSServerWithPing, onPingDone: CSOperation<*>.(CSConcurrentProcess) -> Unit)
    : CSPingRequest<List<Any>>(server, { CSConcurrentProcess().apply { onPingDone(this@apply) } })

open class CSPingRequest<Data : Any>(server: CSServerWithPing, onPingDone: CSOperation<*>.() -> CSProcess<Data>)
    : CSOperation<Data>({ CSPingMultiProcess(server) { onPingDone() } })

open class CSPingMultiRequest<Data : Any>(server: CSServerWithPing, items: Data,
                                          onPingDone: CSOperation<*>.(CSMultiProcess<Data>) -> Unit)
    : CSOperation<Data>({ CSPingMultiProcess(server, items) { onPingDone(this@CSPingMultiProcess) } })

open class CSPingMultiProcess<Data : Any> : CSMultiProcess<Data> {

    constructor(server: CSServerWithPing, onPingDone: () -> CSProcess<Data>)
            : this(server, null, { addLast(onPingDone()) })

    constructor(server: CSServerWithPing, data: Data?, onPingDone: CSMultiProcess<Data>.() -> Unit)
            : super(data) {
        addedProcess = server.ping().onSuccess(onPingSuccess(onPingDone))
                .onFailed { addedProcess = server.ping().onDone(onPingSuccess(onPingDone)) }
    }

    private fun onPingSuccess(onPingDone: CSMultiProcess<Data>.() -> Unit)
            : (CSProcess<CSServerData>) -> Unit = { if (!it.isCanceled) onPingDone(this) }
}