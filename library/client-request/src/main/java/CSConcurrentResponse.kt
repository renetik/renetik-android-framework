package renetik.android.client.request

import renetik.android.java.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll

open class CSConcurrentResponse(data: MutableList<Any>) : CSResponse<List<Any>>(data) {

    private val responses: MutableList<CSResponse<*>> = list()
    private val runningResponses: MutableList<CSResponse<*>> = list()

    constructor() : this(list())

    constructor(vararg adding: CSResponse<*>) : this() {
        runningResponses.putAll(responses.putAll(adding)).forEach { response ->
            response.onSuccess { onResponseSuccess(it) }
            response.onFailed { onResponseFailed(it) }
        }
    }

    fun add(response: CSResponse<*>) =
            runningResponses.put(responses.put(response))
                    .onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }

    private fun onResponseSuccess(successResponse: CSResponse<*>) {
        if (runningResponses.apply { remove(successResponse) }.isEmpty())
            success((data as MutableList).apply {
                responses.forEach { response -> add(response.data) }
            })
    }

    private fun onResponseFailed(failedResponse: CSResponse<*>) {
        runningResponses.apply { remove(failedResponse) }.forEach { response -> response.cancel() }
        failed(failedResponse)
    }

    override fun cancel() {
        runningResponses.forEach { it.cancel() }
        super.cancel()
    }
}

private fun <E> MutableList<E>.kokot(adding: Array<E>) = apply {

}
