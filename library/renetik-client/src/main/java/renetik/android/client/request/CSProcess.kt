package renetik.android.client.request

import renetik.android.framework.CSContextController
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.rootCauseMessage
import renetik.android.logging.CSLog.logDebug
import renetik.android.logging.CSLog.logError
import renetik.android.logging.CSLog.logInfo
import renetik.android.util.CSSynchronizedProperty.Companion.synchronize

open class CSProcess<Data : Any>(var data: Data? = null) : CSContextController() {

    private val eventSuccess = event<CSProcess<Data>>()
    fun onSuccess(function: (CSProcess<Data>) -> Unit) = apply { eventSuccess.listen(function) }

    private val eventCancel = event<CSProcess<Data>>()
    fun onCancel(function: (CSProcess<Data>) -> Unit) = apply { eventCancel.listen(function) }

    private val eventFailed = event<CSProcess<*>>()
    fun onFailed(function: (CSProcess<*>) -> Unit) = apply { eventFailed.listen(function) }

    private val eventDone = event<CSProcess<Data>>()
    fun onDone(function: (CSProcess<Data>) -> Unit) = apply { eventDone.listen(function) }

    private val onProgress = event<CSProcess<Data>>()

    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess = false
    var isFailed = false
    var isDone = false
    var isCanceled by synchronize(false)
    var title: String? = null
    var failedMessage: String? = null
    var failedProcess: CSProcess<*>? = null
    var throwable: Throwable? = null

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
        logInfo("Response onSuccessImpl", this)
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
        failedMessage = (process.failedMessage?.let { "$it, " } ?: "") +
                process.throwable?.rootCauseMessage
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
        eventCancel.fire(this)
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

open class CSHttpProcess<Data : Any>(url: String, data: Data) : CSProcess<Data>(data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}