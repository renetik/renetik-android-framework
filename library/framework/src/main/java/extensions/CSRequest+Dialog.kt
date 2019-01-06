package renetik.android.framework.extensions

import renetik.android.client.request.CSRequest
import renetik.android.controller.base.root
import renetik.android.dialog.extensions.dialog

fun <Data : Any> CSRequest<Data>.send(title: String, withProgress: Boolean) =
        if (withProgress) sendWithProgress(title) else sendWithFailedDialog(title)

fun <Data : Any> CSRequest<Data>.sendWithProgress(title: String): CSRequest<Data> = apply {
    val response = send()
    val progress = root!!.dialog(title).showIndeterminateProgress("Retry", {
        response.cancel()
        sendWithProgress(title)
    }, "Cancel", { cancel() })
    response.onFailed {
        root!!.dialog(title, "Operation failed")
                .show("Retry", { sendWithProgress(title) }, "Ok", { cancel() })
    }.onDone { progress.hide() }
}

fun <Data : Any> CSRequest<Data>.sendWithFailedDialog(title: String): CSRequest<Data> = apply {
    send().onFailed {
        root!!.dialog(title, "Operation failed")
                .show("Retry", { sendWithFailedDialog(title) }, "Ok", { cancel() })
    }
}