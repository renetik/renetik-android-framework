package renetik.android.controller.base

import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import renetik.android.core.extensions.content.getDeniedPermissions
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.core.math.CSMath.randomInt
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus

fun CSActivity.requestPermissions(
    permissions: List<String>,
    onGranted: (() -> Unit)? = null, onNotGranted: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
) {
    val deniedPermissions = getDeniedPermissions(permissions)
    if (deniedPermissions.isSet) {
        val requestCode = randomInt(0, 999)
        ActivityCompat.requestPermissions(this, deniedPermissions, requestCode)
        var registration: CSRegistration? = null
        registration = this + onRequestPermissionsResult.listen { results ->
            if (results.requestCode == requestCode) {
                registration?.cancel()
                for (status in results.statuses) if (PERMISSION_GRANTED != status) {
                    onNotGranted?.invoke()
                    onDone?.invoke()
                    return@listen
                }
                onGranted?.invoke()
                onDone?.invoke()
            }
        }
    } else {
        onGranted?.invoke()
        onDone?.invoke()
    }
}