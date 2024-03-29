package renetik.android.controller.extensions

import android.content.*
import android.content.Intent.*
import androidx.appcompat.app.AppCompatActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.CSStartActivityResult.ActivityNotFound
import renetik.android.controller.extensions.CSStartActivityResult.Cancel
import renetik.android.core.kotlin.primitives.random
import renetik.android.event.listen
import renetik.android.event.registration.cancel
import renetik.android.event.registration.plus

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
        this + activity().onActivityResult.listen { registration, result ->
            if (result.requestCode == requestCode) {
                if (result.isOK()) onSuccess(result.data)
                else onFailure?.invoke(Cancel)
                cancel(registration)
            }
        }
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
