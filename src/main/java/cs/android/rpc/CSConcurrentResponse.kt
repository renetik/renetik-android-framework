package cs.android.rpc

import cs.java.collections.CSList
import cs.java.lang.CSLang.doLater
import cs.java.lang.CSLang.list

class CSConcurrentResponse(val responses: CSList<CSResponse<*>>) : CSResponse<CSList<Any>>(list()) {

    private val failedResponses = list<CSResponse<*>>()
    private val successResponses = list<CSResponse<*>>()

    init {
        responses.forEach { response ->
            response.onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }
        }
    }

    private fun onResponseSuccess(response: CSResponse<*>) {
        if (responses.apply { remove(successResponses.put(response)) }.isEmpty()) onResponsesDone()
    }

    private fun onResponseFailed(response: CSResponse<*>) {
        if (responses.apply { remove(failedResponses.put(response)) }.isEmpty()) onResponsesDone()
    }

    private fun onResponsesDone() {
        if (failedResponses.hasItems) failed(failedResponses.first())
        else success(list<Any>().apply {
            successResponses.forEach { add(it.data()) }
        })
    }

    override fun cancel() {
        responses.forEach { it.cancel() }
        super.cancel()
    }

    fun onAddDone() {
        doLater { if (responses.isEmpty()) onResponsesDone() }
    }

    override fun reset() {
        super.reset()
        responses.removeAll()
        failedResponses.removeAll()
    }
}