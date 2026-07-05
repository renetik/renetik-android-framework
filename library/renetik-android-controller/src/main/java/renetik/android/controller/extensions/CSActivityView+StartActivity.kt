package renetik.android.controller.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openUri(uri: String, appPackage: String? = null): Boolean =
    Intent(Intent.ACTION_VIEW, uri.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appPackage?.let(::setPackage)
    }.let { intent ->
        intent.resolveActivity(packageManager)?.run {
            runCatching { startActivity(intent); true }.getOrDefault(false)
        } ?: false
    }