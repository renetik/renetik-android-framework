package renetik.android.controller.extensions

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import renetik.android.controller.base.CSActivityView
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.core.math.CSMath.randomInt
import renetik.android.event.listen
import renetik.android.event.registration.cancel
import renetik.android.event.registration.plus

fun CSActivityView<*>.requestPermissions(permissions: List<String>, onGranted: () -> Unit) {
    requestPermissions(permissions, onGranted, null)
}

fun CSActivityView<*>.requestPermissionsWithForce(
    permissions: List<String>,
    onGranted: () -> Unit,
) {
    requestPermissions(permissions, onGranted,
        onNotGranted = { requestPermissionsWithForce(permissions, onGranted) })
}

fun CSActivityView<*>.requestPermissions(
    permissions: List<String>,
    onGranted: (() -> Unit)? = null, onNotGranted: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
) {
    val deniedPermissions = getDeniedPermissions(permissions)
    if (deniedPermissions.isSet) {
        val requestCode = randomInt(0, 999)
        ActivityCompat.requestPermissions(activity(), deniedPermissions, requestCode)
        this + activity().onRequestPermissionsResult.listen { registration, results ->
            if (results.requestCode == requestCode) {
                cancel(registration)
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

fun CSActivityView<*>.getDeniedPermissions(permissions: List<String>): Array<String> {
    val deniedPermissions = list<String>()
    for (permission in permissions)
        if (isPermissionGranted(permission)) deniedPermissions.add(permission)
    return deniedPermissions.toTypedArray()
}

fun CSActivityView<*>.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED
}

