package renetik.android.client.request

import renetik.android.java.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll

open class CSConcurrentResponse() : CSResponse<List<Any>>() {

    private val responses: MutableList<CSResponse<*>> = list()
    private val runningResponses: MutableList<CSResponse<*>> = list()

    constructor(responses: MutableList<CSResponse<*>>) : this() {
        runningResponses.putAll(responses.putAll(responses)).forEach { response ->
            response.onSuccess { onResponseSuccess(it) }
            response.onFailed { onResponseFailed(it) }
        }
    }

    fun add(response: CSResponse<*>) =
        runningResponses.put(responses.put(response))
                .onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }

    private fun onResponseSuccess(successResponse: CSResponse<*>) {
        if (runningResponses.apply { remove(successResponse) }.isEmpty())
            success(list<Any>().apply { responses.forEach { response -> add(response.data) } })
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