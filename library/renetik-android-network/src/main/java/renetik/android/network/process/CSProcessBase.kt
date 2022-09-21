package renetik.android.network.process

import renetik.android.core.kotlin.rootCause
import renetik.android.core.lang.variable.CSSynchronizedProperty.Companion.synchronized
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLog.logError
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.core.logging.CSLogMessage.Companion.traceMessage
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasDestroy
import renetik.android.event.common.CSModel

open class CSProcessBase<Data : Any>(
    parent: CSHasDestroy? = null,
    var data: Data? = null) : CSModel(parent) {

    val eventSuccess = event<CSProcessBase<Data>>()
    fun onSuccess(function: (CSProcessBase<Data>) -> Unit) = apply { eventSuccess.listen(function) }

    val eventCancel = event<CSProcessBase<Data>>()
    fun onCancel(function: (CSProcessBase<Data>) -> Unit) = apply { eventCancel.listen(function) }

    val eventFailed = event<CSProcessBase<*>>()
    fun onFailed(function: (CSProcessBase<*>) -> Unit) = apply { eventFailed.listen(function) }

    val eventDone = event<CSProcessBase<Data>>()
    fun onDone(function: (CSProcessBase<Data>) -> Unit) = apply { eventDone.listen(function) }

    val onProgress = event<CSProcessBase<Data>>()

    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess = false
    var isFailed = false
    var isDone = false
    var isCanceled by synchronized(false)
    var title: String? = null
    var failedMessage: String? = null
    var failedProcess: CSProcessBase<*>? = null
    var throwable: Throwable? = null

    fun success() {
        if (isCanceled) return
        if (isDestroyed) return
        onSuccessImpl()
        onDoneImpl()
    }

    fun success(data: Data) {
        if (isCanceled) return
        if (isDestroyed) return
        this.data = data
        onSuccessImpl()
        onDoneImpl()
    }

    private fun onSuccessImpl() {
        logDebug { message("Response onSuccessImpl $this, $data") }
        if (isFailed) logError { traceMessage("already failed") }
        if (isSuccess) logError { traceMessage("already success") }
        if (isDone) logError { traceMessage("already done") }
        isSuccess = true
        eventSuccess.fire(this)
    }

    fun failed(process: CSProcessBase<*>) {
        if (isCanceled) return
        if (isDestroyed) return
        onFailedImpl(process)
        onDoneImpl()
    }

    fun failed(message: String): CSProcessBase<Data> {
        if (isCanceled) return this
        if (isDestroyed) return this
        this.failedMessage = message
        failed(this)
        return this
    }

    fun failed(exception: Throwable?, message: String? = null) {
        if (isCanceled) return
        if (isDestroyed) return
        this.throwable = exception
        this.failedMessage = message
        failed(this)
    }

    private fun onFailedImpl(process: CSProcessBase<*>) {
        if (isDone) logError { traceMessage("already done") }
        if (isFailed) logError { traceMessage("already failed") }
        else isFailed = true
        failedProcess = process
        failedMessage = process.failedMessage
        process.throwable?.rootCause?.let { logWarn { message(it) } }
        throwable = process.throwable ?: Throwable()
        logError { message(throwable, failedMessage) }
        eventFailed.fire(process)
    }

    open fun cancel() {
        logDebug {
            message("Response cancel, $this, isDestroyed:$isDestroyed, " +
                    "isCanceled:$isCanceled, isDone:$isDone, " +
                    "isSuccess:$isSuccess, isFailed:$isFailed")
        }
        if (isDestroyed || isCanceled || isDone || isSuccess || isFailed) return
        isCanceled = true
        eventCancel.fire(this)
        onDoneImpl()
    }

    private fun onDoneImpl() {
        logDebug { message("Response onDone: $this") }
        if (isDone) {
            logError { traceMessage("already done") }
            return
        }
        isDone = true
        eventDone.fire(this)
        onDestroy()
    }

    override fun toString() = "${super.toString()} data:$data"
}