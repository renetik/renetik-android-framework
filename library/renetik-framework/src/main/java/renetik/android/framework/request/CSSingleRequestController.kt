package renetik.android.framework.request

import android.widget.ProgressBar
import renetik.android.client.request.CSOperation
import renetik.android.controller.base.CSViewController
import renetik.android.java.common.CSConstants.SECOND
import renetik.android.material.extensions.snackBarInfo
import renetik.android.task.CSDoLater
import renetik.android.task.later
import renetik.android.view.extensions.hide
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.show

open class CSSingleRequestController(parent: CSViewController<*>, viewId: Int)
    : CSViewController<ProgressBar>(parent, viewId) {

    var currentOperation: CSOperation<*>? = null
    var retryTimer: CSDoLater? = null
    var requestTitle: String? = null

    init {
        view.onClick {
            retryTimer?.stop()
            send()
        }
    }

    open fun <T : Any> send(operation: CSOperation<T>, title: String): CSOperation<T> {
        retryTimer?.stop()
        currentOperation?.cancel()
        view.show()
        currentOperation = operation.onDone { view.hide() }
        requestTitle = title
        send()
        return operation
    }

    private fun send() {
        currentOperation?.send()?.onFailed {
            snackBarInfo("$requestTitle not successful, retrying in 3 sec.")
            retryTimer = later(3 * SECOND) {
                retryTimer = null
                send()
            }
        }
    }

}