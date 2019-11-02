package renetik.android.framework.extensions

import renetik.android.client.request.CSOperation
import renetik.android.controller.base.root
import renetik.android.dialog.extensions.dialog
import renetik.android.framework.R

fun <Data : Any> CSOperation<Data>.send(title: String, progress: Boolean) =
    if (progress) sendWithProgress(title) else sendWithFailedDialog(title)

fun <Data : Any> CSOperation<Data>.sendWithProgress(title: String): CSOperation<Data> = apply {
    val response = send()
    val progress = root!!.dialog(title)
        .showIndeterminateProgress(getString(R.string.renetik_android_framework_send_request_retry), {
            response.cancel()
            sendWithProgress(title)
        }, getString(R.string.renetik_android_framework_send_request_cancel), { cancel() })
    response.onFailed {
        root!!.dialog(title, getString(R.string.renetik_android_framework_send_request_failed))
            .show(
                getString(R.string.renetik_android_framework_send_request_retry), { sendWithProgress(title) },
                getString(R.string.renetik_android_framework_send_request_cancel), { cancel() })
    }.onDone { progress.hide() }
}

fun <Data : Any> CSOperation<Data>.sendWithFailedDialog(title: String): CSOperation<Data> = apply {
    send().onFailed {
        root!!.dialog(title, getString(R.string.renetik_android_framework_send_request_failed))
            .show(
                getString(R.string.renetik_android_framework_send_request_retry),
                { sendWithFailedDialog(title) },
                getString(R.string.renetik_android_framework_send_request_cancel),
                { cancel() })
    }
}