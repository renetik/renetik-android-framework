package renetik.android.controller.extensions

import android.content.*
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.CSStartActivityResult.ActivityNotFound
import renetik.android.controller.extensions.CSStartActivityResult.Cancel
import renetik.android.core.kotlin.primitives.random
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.event.listen
import renetik.android.event.registration.cancel
import renetik.android.event.registration.register

fun CSActivityView<*>.startActivityForResult(
    activityClass: Class<out AppCompatActivity>, requestCode: Int) =
    startActivityForResult(Intent(activity(), activityClass), requestCode)

enum class CSStartActivityResult {
    Cancel, ActivityNotFound
}

fun CSActivityView<*>.startActivityForResult(
    intent: Intent, onSuccess: (Intent?) -> Unit,
    onFailure: ((CSStartActivityResult) -> Unit)? = null) {
    try {
        val requestCode = Int.random(0, 9999)
        startActivityForResult(intent, requestCode)
        register(activity().onActivityResult.listen { registration, result ->
            if (result.requestCode == requestCode) {
                if (result.isOK()) onSuccess(result.data)
                else onFailure?.invoke(Cancel)
                cancel(registration)
            }
        })
    } catch (ex: ActivityNotFoundException) {
        onFailure?.invoke(ActivityNotFound)
    }
}

@Suppress("DEPRECATION")
fun CSActivityView<*>.startActivityForResult(intent: Intent, requestCode: Int) =
    activity().startActivityForResult(intent, requestCode)

fun CSActivityView<*>.switchActivity(activityClass: Class<out AppCompatActivity>) =
    switchActivity(Intent(activity(), activityClass))

fun CSActivityView<*>.switchActivity(intent: Intent) {
    activity().finish()
    startActivity(intent)
}

fun CSActivityView<*>.switchActivity(activityClass: Class<out AppCompatActivity>,
                                     resultCode: Int) {
    activity().setResult(resultCode)
    switchActivity(Intent(activity(), activityClass))
}

//fun CSActivityView<*>.restartActivity() = later {
//    val intent = activity().intent
//    activity().finish()
//    startActivity(intent)
//}

@Deprecated("Move to Context+")
fun Context.goHome() =
    startActivity(Intent(ACTION_MAIN).addCategory(CATEGORY_HOME))

@Deprecated("Move to Context+")
fun Context.startApplication(packageName: String) {
    try {
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.LAUNCHER")
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
        for (info in resolveInfoList)
            if (info.activityInfo.packageName.equals(packageName, ignoreCase = true)) {
                launchComponent(info.activityInfo.packageName, info.activityInfo.name)
                return
            }
        showInMarket(packageName)
    } catch (e: Exception) {
        showInMarket(packageName)
    }
}

@Deprecated("Move to Context+")
private fun Context.launchComponent(packageName: String, name: String) {
    val intent = Intent("android.intent.action.MAIN")
    intent.addCategory("android.intent.category.LAUNCHER")
    intent.component = ComponentName(packageName, name)
    intent.flags = FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

@Deprecated("Move to Context+")
private fun Context.showInMarket(packageName: String?) {
    val intent = Intent(ACTION_VIEW, Uri.parse("market://details?id=" + packageName!!))
    intent.flags = FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

@Deprecated("Move to Context+")
fun Context.startActivityForUri(
    uri: Uri, onActivityNotFound: ((ActivityNotFoundException) -> Unit)? = null) =
    startActivityForUriAndType(uri, null, onActivityNotFound)

@Deprecated("Move to Context+")
fun Context.startActivityForUriAndType(
    uri: Uri, type: String?, onActivityNotFound: ((ActivityNotFoundException) -> Unit)? = null) {
    val intent = Intent(ACTION_VIEW)
    intent.setDataAndType(uri, type)
    // Grant Permission to a Specific Package
    // https://developer.android.com/reference/androidx/core/content/FileProvider
    intent.flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_GRANT_READ_URI_PERMISSION
    intent.clipData = ClipData.newRawUri("", uri)
    try {
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        logWarn { message(exception) }
        onActivityNotFound?.invoke(exception)
    }
}

@Deprecated("Move to Context+")
fun Context.openUrl(url: String) =
    startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
