package renetik.android.network.process

import renetik.android.core.kotlin.rootCause
import renetik.android.core.lang.variable.CSSynchronizedProperty.Companion.synchronized
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLog.logError
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.core.logging.CSLogMessage.Companion.traceMessage
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.registration.later

open class CSProcess<Data : Any>(
    parent: CSHasDestruct? = null,
    var data: Data? = null) : CSModel(parent) {

    companion object {
        fun <Data : Any> CSProcess(
            parent: CSHasDestruct, function: CSProcess<Data>.() -> Unit)
                : CSProcess<Data> = CSProcess<Data>(parent).also {
            it.later { function(it) }
        }
    }

    val eventSuccess = event<CSProcess<Data>>()
    fun onSuccess(function: (CSProcess<Data>) -> Unit) = apply { eventSuccess.listen(function) }

    val eventCancel = event<CSProcess<Data>>()
    fun onCancel(function: (CSProcess<Data>) -> Unit) = apply { eventCancel.listen(function) }

    val eventFailed = event<CSProcess<*>>()
    fun onFailed(function: (CSProcess<*>) -> Unit) = apply { eventFailed.listen(function) }

    val eventDone = event<CSProcess<Data>>()
    fun onDone(function: (CSProcess<Data>) -> Unit) = apply { eventDone.listen(function) }

    val onProgress = event<CSProcess<Data>>()

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
    var failedProcess: CSProcess<*>? = null
    var throwable: Throwable? = null

    fun success() {
        if (isCanceled) return
        if (isDestructed) return
        onSuccessImpl()
        onDoneImpl()
    }

    fun success(data: Data) {
        if (isCanceled) return
        if (isDestructed) return
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

    fun failed(message: String) {
        if (isCanceled) return
        if (isDestructed) return
        this.failedMessage = message
        failed(this)
    }

    fun failed(exception: Throwable?, message: String? = null) {
        if (isCanceled) return
        if (isDestructed) return
        this.throwable = exception
        this.failedMessage = message
        failed(this)
    }

    fun failed(process: CSProcess<*>) {
        if (isCanceled) return
        if (isDestructed) return
        onFailedImpl(process)
        onDoneImpl()
    }

    private fun onFailedImpl(process: CSProcess<*>) {
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
            message("Response cancel, $this, isDestroyed:$isDestructed, " +
                    "isCanceled:$isCanceled, isDone:$isDone, " +
                    "isSuccess:$isSuccess, isFailed:$isFailed")
        }
        if (isDestructed || isCanceled || isDone || isSuccess || isFailed) return
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
        onDestruct()
    }

    override fun toString() = "${super.toString()} data:$data"
}