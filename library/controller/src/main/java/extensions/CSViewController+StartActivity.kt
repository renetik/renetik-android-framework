package renetik.android.controller.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import renetik.android.controller.base.CSViewController
import renetik.android.logging.CSLog

fun <T : CSViewController<*>> T.startActivityForUri(uri: Uri, onActivityNotFound: ((ActivityNotFoundException) -> Unit)?) =
        startActivityForUriAndType(uri, null, onActivityNotFound)

fun <T : CSViewController<*>> T.startActivityForUriAndType(uri: Uri, type: String?,
                                                           onActivityNotFound: ((ActivityNotFoundException) -> Unit)?) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    try {
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        CSLog.logWarn(exception)
        onActivityNotFound?.invoke(exception)
    }
}