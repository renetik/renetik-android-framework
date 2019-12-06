package renetik.android.client.request

import renetik.android.base.CSContextController
import renetik.android.java.event.event
import renetik.android.java.event.register
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.getRootCauseMessage
import renetik.android.logging.CSLog.logDebug
import renetik.android.logging.CSLog.logError
import renetik.android.logging.CSLog.logInfo

open class CSProcess<Data : Any> : CSContextController {

    private val eventSuccess = event<CSProcess<Data>>()
    fun onSuccess(function: (CSProcess<Data>) -> Unit) = apply { eventSuccess.register(function) }

    private val eventFailed = event<CSProcess<*>>()
    fun onFailed(function: (CSProcess<*>) -> Unit) = apply { eventFailed.register(function) }

    private val eventDone = event<CSProcess<Data>>()
    fun onDone(function: (CSProcess<Data>) -> Unit) = apply { eventDone.register(function) }

    private val onProgress = event<CSProcess<Data>>()

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
    var failedProcess: CSProcess<*>? = null
    var throwable: Throwable? = null

    constructor(url: String, data: Data) {
        this.url = url
        this.data = data
    }

    constructor(data: Data? = null) {
        data?.let { this.data = it }
    }

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

    fun failed(process: CSProcess<*>) {
        if (isCanceled) return
        onFailedImpl(process)
        onDoneImpl()
    }

    fun failed(message: String): CSProcess<Data> {
        if (isCanceled) return this
        this.failedMessage = message
        failed(this)
        return this
    }

    fun failed(exception: Throwable?, message: String? = null) {
        if (isCanceled) return
        this.throwable = exception
        this.failedMessage = message
        failed(this)
    }

    private fun onFailedImpl(process: CSProcess<*>) {
        if (isDone) logError(exception("already done"))
        if (isFailed) logError(exception("already failed"))
        failedProcess = process
        isFailed = true
        failedMessage = "${process.failedMessage}, ${process.throwable?.getRootCauseMessage()}"
        throwable = process.throwable ?: Throwable()
        logError(throwable!!, failedMessage)
        eventFailed.fire(process)
    }

    open fun cancel() {
        logDebug(
            "Response cancel", this, "isCanceled", isCanceled,
            "isDone", isDone, "isSuccess", isSuccess, "isFailed", isFailed
        )
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

