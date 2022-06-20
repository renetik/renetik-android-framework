package renetik.android.framework.network

import android.view.ViewGroup
import renetik.android.network.operation.CSOperation
import renetik.android.network.process.CSProcessBase
import renetik.android.core.extensions.content.isNetworkConnected
import renetik.android.controller.base.CSActivityView
import renetik.android.core.CSApplication.Companion.app
import renetik.android.core.kotlin.unfinished

//fun <Data : Any> CSOperation<Data>.send(
//    parent: CSActivityView<out ViewGroup>,
//    title: String, isProgress: Boolean = true, isFailedDialog: Boolean = true,
//    onInternetFailed: (() -> Unit)? = null,
//    onSuccess: ((Data) -> Unit)? = null) = apply {
//    val process = send()
//    if (isProgress) {
//        throw unfinished // TODO!!!
//        val progress = parent.dialog().showProgress(title,
//            cancelTitle = getString(R.string.renetik_android_framework_operation_send_cancel)) { cancel() }
//        process.onDone { progress.hide() }
//    }
//    if (isFailedDialog) onSendFailed(parent, process, title, isProgress,
//        onInternetFailed, onSuccess)
//    else process.onFailed { cancel() }
//    onSuccess?.let { this.onSuccess(it) }
//}

private fun <Data : Any> CSOperation<Data>.onSendFailed(
    parent: CSActivityView<out ViewGroup>,
    process: CSProcessBase<Data>, title: String, isProgress: Boolean,
    onInternetFailed: (() -> Unit)?,
    onSuccess: ((Data) -> Unit)?) {
    process.onFailed {
        if (!app.isNetworkConnected && onInternetFailed != null) onInternetFailed()
        else unfinished() // TODO!!!
//            parent.dialog(title,
//            getString(R.string.renetik_android_framework_operation_send_failed))
//            .show(getString(R.string.renetik_android_framework_operation_send_cancel), {
//                cancel()
//            }, getString(R.string.renetik_android_framework_operation_send_retry), {
//                send(parent, title, isProgress, true, onInternetFailed, onSuccess)
//            })
    }
}

//fun <Data : Any> CSOperation<Data>.sendSilently(
//    parent: CSActivityView<out ViewGroup>,
//    onInternetFailed: (() -> Unit)? = null,
//    onSuccess: ((Data) -> Unit)? = null) =
//    send(parent, "", isProgress = false, isFailedDialog = false,
//        onInternetFailed, onSuccess)

//fun <Data : Any> CSOperation<Data>.sendProgress(
//    parent: CSActivityView<out ViewGroup>,
//    title: String, onInternetFailed: (() -> Unit)? = null,
//    onSuccess: ((Data) -> Unit)? = null) =
//    send(parent, title, isProgress = true, isFailedDialog = false,
//        onInternetFailed, onSuccess)

//fun <Data : Any> CSOperation<Data>.sendWithProgressAndDescriptiveDialog(
//    parent: CSActivityView<out ViewGroup>,
//    title: String, onInternetFailed: (() -> Unit)? = null,
//    onSuccess: ((Data) -> Unit)? = null): CSOperation<Data> = apply {
//    val process = send()
//    throw unfinished // TODO!!!
//    val progress = parent.dialog().showProgress(title,
//        cancelTitle = getString(R.string.renetik_android_framework_operation_send_cancel)) { cancel() }
//    process.onFailed {
//        if (!application.isNetworkConnected && onInternetFailed != null)
//            onInternetFailed()
//        else
//            parent.dialog(title,
//                it.failedMessage
//                    ?: getString(R.string.renetik_android_framework_operation_send_failed))
//                .show(
//                    getString(R.string.renetik_android_framework_operation_send_cancel),
//                    { cancel() },
//                    getString(R.string.renetik_android_framework_operation_send_retry),
//                    {
//                        sendWithProgressAndDescriptiveDialog(parent,
//                            title, onInternetFailed, onSuccess)
//                    })
//    }.onDone { progress.hide() }
//    onSuccess?.let { this.onSuccess(it) }
//    }
//}