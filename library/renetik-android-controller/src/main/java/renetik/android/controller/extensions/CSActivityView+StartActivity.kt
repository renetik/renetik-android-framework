package renetik.android.controller.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

//@Deprecated("DEPRECATION")
//fun CSActivityView<*>.startActivityForResult(
//    activityClass: Class<out AppCompatActivity>, requestCode: Int
//) = startActivityForResult(Intent(activity(), activityClass), requestCode)
//
//@Deprecated("DEPRECATION")
//enum class CSStartActivityResult { Cancel, ActivityNotFound }
//
//@Deprecated("DEPRECATION")
//fun CSActivityView<*>.startActivityForResult(
//    intent: Intent, onSuccess: (Intent?) -> Unit,
//    onFailure: ((CSStartActivityResult) -> Unit)? = null
//) {
//    try {
//        val requestCode = Int.random(0, 9999)
//        startActivityForResult(intent, requestCode)
//        var registration: CSRegistration? = null
//        registration = this + activity().onActivityResult.listen { result ->
//            if (result.requestCode == requestCode) {
//                if (result.isOK()) onSuccess(result.data)
//                else onFailure?.invoke(Cancel)
//                registration?.cancel()
//            }
//        }
//    } catch (ex: ActivityNotFoundException) {
//        onFailure?.invoke(ActivityNotFound)
//    }
//}
//
//@Deprecated("DEPRECATION")
//fun CSActivityView<*>.startActivityForResult(intent: Intent, requestCode: Int) =
//    activity().startActivityForResult(intent, requestCode)

fun Context.openUri(uri: String, appPackage: String? = null): Boolean =
    Intent(Intent.ACTION_VIEW, uri.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appPackage?.let(::setPackage)
    }.let { intent ->
        intent.resolveActivity(packageManager)?.run {
            runCatching { startActivity(intent); true }.getOrDefault(false)
        } ?: false
    }