package renetik.android.client.request

import renetik.android.java.event.event
import renetik.android.java.event.execute

class CSRequest<Data : Any>(val onSend: () -> CSResponse<Data>) {
    private val eventSuccess = event<Data>()
    private val eventFailed = event<CSResponse<*>>()
    private val eventDone = event<Data>()
    var response: CSResponse<Data>? = null

    fun onSuccess(function: (argument: Data) -> Unit) =
            apply { eventSuccess.execute(function) }

    fun onFailed(function: (argument: CSResponse<*>) -> Unit) =
            apply { eventFailed.execute(function) }

    fun onDone(function: (argument: Data) -> Unit) =
            apply { eventDone.execute(function) }

    fun send(): CSResponse<Data> = onSend().apply {
        response = this
        onSuccess {
            eventSuccess.fire(data)
            eventDone.fire(data)
        }
    }

    fun cancel() {
        response!!.apply {
            if (isFailed) {
                eventFailed.fire(this)
                eventDone.fire(data)
            } else {
                cancel()
                eventDone.fire(data)
            }
        }
    }
}