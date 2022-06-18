package renetik.android.framework.request

import android.widget.ProgressBar
import renetik.android.client.request.CSOperation
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.snackBarInfo
import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.event.registration.CSRegistration
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.show

open class CSSingleRequestView(parent: CSActivityView<*>, viewId: Int)
    : CSActivityView<ProgressBar>(parent, viewId) {

    var currentOperation: CSOperation<*>? = null
    var retryTimer: CSRegistration? = null
    var requestTitle: String? = null

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
            snackBarInfo("$requestTitle not successful, retrying in 3 sec.")
            retryTimer = later(3 * Second) {
                retryTimer = null
                send()
            }
        }
    }

}