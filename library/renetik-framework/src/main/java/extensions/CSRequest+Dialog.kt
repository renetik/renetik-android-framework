package renetik.android.framework.extensions

import android.widget.ProgressBar
import renetik.android.client.request.CSRequest
import renetik.android.controller.base.root
import renetik.android.dialog.extensions.dialog
import renetik.android.view.extensions.show

fun <Data : Any> CSRequest<Data>.send(title: String, progress: Boolean) =
        if (progress) sendWithProgress(title) else sendWithFailedDialog(title)

fun <Data : Any> CSRequest<Data>.sendWithProgress(title: String): CSRequest<Data> = apply {
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

fun <Data : Any> CSRequest<Data>.sendWithFailedDialog(title: String): CSRequest<Data> = apply {
    send().onFailed {
        root!!.dialog("Operation failed", title)
                .show("Retry", { sendWithFailedDialog(title) }, "Ok", { cancel() })
    }
}