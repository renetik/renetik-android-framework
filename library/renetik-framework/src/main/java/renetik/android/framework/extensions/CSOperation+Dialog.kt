package renetik.android.framework.extensions

import renetik.android.client.request.CSOperation
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.controller.extensions.dialog
import renetik.android.framework.R

fun <Data : Any> CSOperation<Data>.send(title: String,
                                        progress: Boolean = true,
                                        onSuccess: ((Data) -> Unit)? = null) =
    if (progress) sendWithProgress(title, onSuccess) else sendWithFailedDialog(title, onSuccess)

private fun <Data : Any> CSOperation<Data>.sendWithProgress(title: String,
                                                            onSuccess: ((Data) -> Unit)? = null)
        : CSOperation<Data> = apply {
    val process = send()

    val progress = navigation.dialog().showProgress(title,
        cancelTitle = getString(R.string.renetik_android_framework_send_request_cancel)) { cancel() }

    process.onFailed {
        navigation.dialog(title, getString(R.string.renetik_android_framework_send_request_failed))
            .show(
                getString(R.string.renetik_android_framework_send_request_retry),
                { sendWithProgress(title, onSuccess) },
                getString(R.string.renetik_android_framework_send_request_cancel), { cancel() })
    }.onDone { progress.hide() }

    onSuccess?.let { this.onSuccess(it) }
}

private fun <Data : Any> CSOperation<Data>.sendWithFailedDialog(
    title: String, onSuccess: ((Data) -> Unit)? = null): CSOperation<Data> =
    apply {
        onSuccess?.let { this.onSuccess(it) }
        send().onFailed {
            navigation.dialog(title,
                getString(R.string.renetik_android_framework_send_request_failed))
                .show(
                    getString(R.string.renetik_android_framework_send_request_retry),
                    { sendWithFailedDialog(title) },
                    getString(R.string.renetik_android_framework_send_request_cancel),
                    { cancel() })
        }
    }

fun <Data : Any> CSOperation<Data>.sendSilently(
    onSuccess: ((Data) -> Unit)? = null) = apply {
    onSuccess?.let { this.onSuccess(it) }
    send().onFailed { cancel() }
}