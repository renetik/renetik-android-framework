package renetik.android.framework.extensions

import android.view.ViewGroup
import renetik.android.client.request.CSOperation
import renetik.android.client.request.CSProcess
import renetik.android.content.isNetworkConnected
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.dialog
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.R

fun <Data : Any> CSOperation<Data>.send(
    parent: CSActivityView<out ViewGroup>,
    title: String, isProgress: Boolean = true, isFailedDialog: Boolean = true,
    onInternetFailed: (() -> Unit)? = null,
    onSuccess: ((Data) -> Unit)? = null) = apply {
    val process = send()
    if (isProgress) {
        val progress = parent.dialog().showProgress(title,
            cancelTitle = getString(R.string.renetik_android_framework_operation_send_cancel)) { cancel() }
        process.onDone { progress.hide() }
    }
    if (isFailedDialog) onSendFailed(parent,process, title, isProgress,
        onInternetFailed, onSuccess)
    else process.onFailed { cancel() }
    onSuccess?.let { this.onSuccess(it) }
}

private fun <Data : Any> CSOperation<Data>.onSendFailed(
    parent: CSActivityView<out ViewGroup>,
    process: CSProcess<Data>, title: String, isProgress: Boolean,
    onInternetFailed: (() -> Unit)?,
    onSuccess: ((Data) -> Unit)?) {
    process.onFailed {
        if (!application.isNetworkConnected && onInternetFailed != null)
            onInternetFailed()
        else parent.dialog(title,
            getString(R.string.renetik_android_framework_operation_send_failed))
            .show(getString(R.string.renetik_android_framework_operation_send_cancel), {
                cancel()
            }, getString(R.string.renetik_android_framework_operation_send_retry), {
                send(parent, title, isProgress, true, onInternetFailed, onSuccess)
            })
    }
}

fun <Data : Any> CSOperation<Data>.sendSilently(
    parent: CSActivityView<out ViewGroup>,
    onInternetFailed: (() -> Unit)? = null,
    onSuccess: ((Data) -> Unit)? = null) =
    send(parent, "", isProgress = false, isFailedDialog = false,
        onInternetFailed, onSuccess)

fun <Data : Any> CSOperation<Data>.sendProgress(
    parent: CSActivityView<out ViewGroup>,
    title: String, onInternetFailed: (() -> Unit)? = null,
    onSuccess: ((Data) -> Unit)? = null) =
    send(parent, title, isProgress = true, isFailedDialog = false,
        onInternetFailed, onSuccess)

fun <Data : Any> CSOperation<Data>.sendWithProgressAndDescriptiveDialog(
    parent: CSActivityView<out ViewGroup>,
    title: String, onInternetFailed: (() -> Unit)? = null,
    onSuccess: ((Data) -> Unit)? = null): CSOperation<Data> = apply {
    val process = send()
    val progress = parent.dialog().showProgress(title,
        cancelTitle = getString(R.string.renetik_android_framework_operation_send_cancel)) { cancel() }
    process.onFailed {
        if (!application.isNetworkConnected && onInternetFailed != null)
            onInternetFailed()
        else
            parent.dialog(title,
                it.failedMessage
                    ?: getString(R.string.renetik_android_framework_operation_send_failed))
                .show(
                    getString(R.string.renetik_android_framework_operation_send_cancel),
                    { cancel() },
                    getString(R.string.renetik_android_framework_operation_send_retry),
                    {
                        sendWithProgressAndDescriptiveDialog(parent,
                            title, onInternetFailed, onSuccess)
                    })
    }.onDone { progress.hide() }
    onSuccess?.let { this.onSuccess(it) }
}