package renetik.android.client.request


interface CSServerWithPing {
    fun ping(): CSResponse<CSServerData>
}

open class CSPingConcurrentRequest(server: CSServerWithPing, onPingDone: CSRequest<*>.(CSConcurrentResponse) -> Unit)
    : CSPingRequest<List<Any>>(server, { CSConcurrentResponse().apply { onPingDone(this@apply) } })

open class CSPingRequest<Data : Any>(server: CSServerWithPing, onPingDone: CSRequest<*>.() -> CSResponse<Data>)
    : CSRequest<Data>({ CSPingMultiResponse(server) { onPingDone() } })

open class CSPingMultiRequest<Data : Any>(server: CSServerWithPing, items: Data,
                                          onPingDone: CSRequest<*>.(CSMultiResponse<Data>) -> Unit)
    : CSRequest<Data>({ CSPingMultiResponse(server, items) { onPingDone(this@CSPingMultiResponse) } })

open class CSPingMultiResponse<Data : Any> : CSMultiResponse<Data> {

    constructor(server: CSServerWithPing, onPingDone: () -> CSResponse<Data>)
            : this(server, null, { addLast(onPingDone()) })

    constructor(server: CSServerWithPing, data: Data?, onPingDone: CSMultiResponse<Data>.() -> Unit)
            : super(data) {
        addedResponse = server.ping().onSuccess(onPingSuccess(onPingDone))
                .onFailed { addedResponse = server.ping().onDone(onPingSuccess(onPingDone)) }
    }

    private fun onPingSuccess(onPingDone: CSMultiResponse<Data>.() -> Unit)
            : (CSResponse<CSServerData>) -> Unit = { if (!it.isCanceled) onPingDone(this) }
}