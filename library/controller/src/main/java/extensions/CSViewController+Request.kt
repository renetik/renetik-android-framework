package renetik.android.controller.extensions

import renetik.android.client.request.CSRequest
import renetik.android.controller.base.CSViewController
import renetik.android.dialog.extensions.dialog

fun <Data : Any> CSViewController<*>.sendRequest(title: String, request: CSRequest<Data>): CSRequest<Data> {
    val response = request.send()
    val progress = dialog(title).showIndeterminateProgress("Retry", {
        response.cancel()
        sendRequest(title, request)
    }, "Cancel", { request.cancel() })
    response.onFailed {
        dialog(title, "Operation failed")
                .show("Retry", { sendRequest(title, request) }, "Ok", { request.cancel() })
    }.onDone { progress.hide() }
    return request
}