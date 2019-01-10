package renetik.android.client.request

import renetik.android.base.CSContextController
import renetik.android.java.event.event
import renetik.android.java.event.execute
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.getRootCauseMessage
import renetik.android.logging.CSLog.logDebug
import renetik.android.logging.CSLog.logError
import renetik.android.logging.CSLog.logInfo

open class CSResponse<Data : Any> : CSContextController {
    private val eventSuccess = event<CSResponse<Data>>()
    fun onSuccess(function: (CSResponse<Data>) -> Unit) = apply { eventSuccess.execute(function) }

    private val eventFailed = event<CSResponse<*>>()
    fun onFailed(function: (CSResponse<*>) -> Unit) = apply { eventFailed.execute(function) }

    private val eventDone = event<CSResponse<Data>>()
    fun onDone(function: (CSResponse<Data>) -> Unit) = apply { eventDone.execute(function) }

    val onProgress = event<CSResponse<Data>>()
    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess = false
    var isFailed = false
    var isDone = false
    var isCanceled = false
    var url: String? = null
    var title: String? = null
    var data: Data? = null
    var failedMessage: String? = null
    var failedResponse: CSResponse<*>? = null
    var throwable: Throwable? = null

    constructor(url: String, data: Data) {
        this.url = url
        this.data = data
    }

    constructor(data: Data? = null) {
        data?.let { this.data = it }
    }

    fun data() = data!!

    fun success() {
        if (isCanceled) return
        onSuccessImpl()
        onDoneImpl()
    }

    fun success(data: Data) {
        if (isCanceled) return
        this.data = data
        onSuccessImpl()
        onDoneImpl()
    }

    private fun onSuccessImpl() {
        logInfo("Response onSuccessImpl", this, url)
        if (isFailed) logError(exception("already failed"))
        if (isSuccess) logError(exception("already success"))
        if (isDone) logError(exception("already done"))
        isSuccess = true
        eventSuccess.fire(this)
    }

    fun failed(response: CSResponse<*>) {
        if (isCanceled) return
        onFailedImpl(response)
        onDoneImpl()
    }

    fun failed(message: String): CSResponse<Data> {
        if (isCanceled) return this
        this.failedMessage = message
        failed(this)
        return this
    }

    fun failed(exception: Throwable?, message: String? = null) {
        if (isCanceled) return
        this.throwable = exception
        this.failedMessage = message
        onFailedImpl(this)
        onDoneImpl()
    }

    private fun onFailedImpl(response: CSResponse<*>) {
        if (isDone) logError(exception("already done"))
        if (isFailed) logError(exception("already failed"))
        failedResponse = response
        isFailed = true
        failedMessage = "${response.failedMessage}, ${response.throwable?.getRootCauseMessage()}"
        throwable = response.throwable ?: Throwable()
        logError(throwable!!, failedMessage)
        eventFailed.fire(response)
    }

    open fun cancel() {
        logDebug("Response cancel", this, "isCanceled", isCanceled, "isDone", isDone, "isSuccess", isSuccess, "isFailed", isFailed)
        if (isCanceled || isDone || isSuccess || isFailed) return
        isCanceled = true
        onDoneImpl()
    }

    private fun onDoneImpl() {
        logDebug("Response onDone", this)
        if (isDone) {
            logError(exception("already done"))
            return
        }
        isDone = true
        eventDone.fire(this)
    }
}

