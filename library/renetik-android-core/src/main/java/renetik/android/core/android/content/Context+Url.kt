package renetik.android.core.android.content

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import renetik.android.core.logging.CSLog.logWarn

fun Context.startActivityForUri(
    uri: Uri, onActivityNotFound: ((ActivityNotFoundException) -> Unit)? = null
) = startActivityForUriAndType(uri, null, onActivityNotFound)

fun Context.startActivityForUriAndType(
    uri: Uri, type: String? = null,
    onActivityNotFound: ((ActivityNotFoundException) -> Unit)? = null
) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.clipData = ClipData.newRawUri("", uri)
    try {
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        logWarn(exception)
        onActivityNotFound?.invoke(exception)
    }
}

fun Context.openUrl(url: String, errorMessage: String? = null) {
    var intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    fun startChooser() = try {
        intent = Intent.createChooser(intent, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        errorMessage?.also(::toast)
        logWarn(e) { "Failed to open url: $url" }
    }
    runCatching {
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
        else startChooser()
    }.onFailure { startChooser() }
}
