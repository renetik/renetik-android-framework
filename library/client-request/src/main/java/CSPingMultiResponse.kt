package renetik.android.client.request

interface CSServerWithPing {
    fun ping(): CSResponse<CSServerData>
}

open class CSPingMultiResponse<Data : Any>(
        server: CSServerWithPing, data: Data, onPingDone: CSMultiResponse<Data>.() -> Unit)
    : CSMultiResponse<Data>(data) {
    init {
        addedResponse = server.ping().onDone { if (!it.isCanceled) onPingDone(this) }
    }
}

class CSPingDataMultiResponse(
        server: CSServerWithPing, onPingDone: CSMultiResponse<CSServerData>.() -> Unit)
    : CSPingMultiResponse<CSServerData>(server, CSServerData(), onPingDone)


open class CSPingResponse<Data : Any>(
        server: CSServerWithPing, onPingDone:() -> CSResponse<Data>)
    : CSMultiResponse<Data>() {
    init {
        addedResponse = server.ping().onDone { if (!it.isCanceled) addLast(onPingDone()) }
    }
}