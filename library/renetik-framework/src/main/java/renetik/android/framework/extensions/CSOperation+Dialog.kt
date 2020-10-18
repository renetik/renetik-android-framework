package renetik.android.framework.extensions

import renetik.android.client.request.CSOperation
import renetik.android.client.request.CSProcess
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.controller.extensions.dialog
import renetik.android.framework.R

fun <Data : Any> CSOperation<Data>.send(
    title: String, isProgress: Boolean = true, isFailedDialog: Boolean = true,
    onSuccess: ((Data) -> Unit)? = null) = apply {
    val process = send()
    if (isProgress) {
        val progress = navigation.dialog().showProgress(title,
            cancelTitle = getString(R.string.renetik_android_framework_operation_send_cancel)) { cancel() }
        process.onDone { progress.hide() }
    }
    if (isFailedDialog) onSendFailed(process, title, isProgress, isFailedDialog, onSuccess)
    else process.onFailed { cancel() }
    onSuccess?.let { this.onSuccess(it) }
}

private fun <Data : Any> CSOperation<Data>.onSendFailed(
    process: CSProcess<Data>, title: String, isProgress: Boolean,
    isFailedDialog: Boolean, onSuccess: ((Data) -> Unit)?) {
    process.onFailed {
        navigation.dialog(title,
            getString(R.string.renetik_android_framework_operation_send_failed))
            .show(getString(R.string.renetik_android_framework_operation_send_retry), {
                send(title, isProgress, isFailedDialog, onSuccess)
            }, getString(R.string.renetik_android_framework_operation_send_cancel), {
                cancel()
            })
    }
}

fun <Data : Any> CSOperation<Data>.sendSilently(onSuccess: ((Data) -> Unit)? = null) =
    send("", isProgress = false, isFailedDialog = false, onSuccess)

fun <Data : Any> CSOperation<Data>.sendProgress(title: String,
                                                onSuccess: ((Data) -> Unit)? = null) =
    send(title, isProgress = true, isFailedDialog = false, onSuccess)