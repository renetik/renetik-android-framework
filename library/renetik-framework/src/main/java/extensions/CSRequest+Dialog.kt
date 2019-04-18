package renetik.android.framework.extensions

import renetik.android.client.request.CSOperation
import renetik.android.controller.base.root
import renetik.android.dialog.extensions.dialog

fun <Data : Any> CSOperation<Data>.send(title: String, progress: Boolean) =
        if (progress) sendWithProgress(title) else sendWithFailedDialog(title)

fun <Data : Any> CSOperation<Data>.sendWithProgress(title: String): CSOperation<Data> = apply {
    val response = send()
    val progress = root!!.dialog(title).showIndeterminateProgress("Retry", {
        response.cancel()
        sendWithProgress(title)
    }, "Cancel", { cancel() })
    response.onFailed {
        root!!.dialog("Operation failed", title)
                .show("Retry", { sendWithProgress(title) }, "Ok", { cancel() })
    }.onDone { progress.hide() }
}

fun <Data : Any> CSOperation<Data>.sendWithFailedDialog(title: String): CSOperation<Data> = apply {
    send().onFailed {
        root!!.dialog("Operation failed", title)
                .show("Retry", { sendWithFailedDialog(title) }, "Ok", { cancel() })
    }
}