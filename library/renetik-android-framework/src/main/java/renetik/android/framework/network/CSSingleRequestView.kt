package renetik.android.framework.network

import android.widget.ProgressBar
import renetik.android.controller.base.CSActivityView
import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.registration.CSRegistration
import renetik.android.network.operation.CSOperation
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.show

open class CSSingleRequestView(parent: CSActivityView<*>, viewId: Int)
    : CSActivityView<ProgressBar>(parent, viewId) {

    var currentOperation: CSOperation<*>? = null
    var retryTimer: CSRegistration? = null
    var requestTitle: String? = null
    var retrySecond = 3
    val eventSendRetry = event()

    init {
        view.onClick {
            retryTimer?.cancel()
            send()
        }
    }

    open fun <T : Any> send(operation: CSOperation<T>, title: String): CSOperation<T> {
        retryTimer?.cancel()
        currentOperation?.cancel()
        view.show()
        currentOperation = operation.onDone { view.gone() }
        requestTitle = title
        send()
        return operation
    }

    private fun send() {
        currentOperation?.send()?.onFailed {
            eventSendRetry.fire()
            logInfo("$requestTitle not successful, retrying in $retrySecond sec.")
            retryTimer = later(retrySecond * Second) {
                retryTimer = null
                send()
            }
        }
    }

}