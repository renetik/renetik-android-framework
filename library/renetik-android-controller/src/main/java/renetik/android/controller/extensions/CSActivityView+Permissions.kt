package renetik.android.controller.extensions

import android.os.Build.VERSION.SDK_INT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import renetik.android.controller.base.CSActivityView
import renetik.android.event.registrations.register
import renetik.android.core.math.CSMath.randomInt
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.event.listen

fun CSActivityView<*>.requestPermissions(permissions: List<String>, onGranted: () -> Unit) {
    requestPermissions(permissions, onGranted, null)
}

fun CSActivityView<*>.requestPermissionsWithForce(permissions: List<String>,
                                                  onGranted: () -> Unit) {
    requestPermissions(permissions,
        onGranted,
        { requestPermissionsWithForce(permissions, onGranted) })
}

fun CSActivityView<*>.requestPermissions(
    permissions: List<String>,
    onGranted: (() -> Unit)? = null, notGranted: (() -> Unit)? = null
) {
    if (SDK_INT < 23) {
        onGranted?.invoke()
        return
    }
    val deniedPermissions = getDeniedPermissions(permissions)
    if (deniedPermissions.isSet) {
        val requestCode = randomInt(0, 999)
        ActivityCompat.requestPermissions(activity(), deniedPermissions, requestCode)
        register(activity().onRequestPermissionsResult.listen {registration, results ->
            if (results.requestCode == requestCode) {
                registration.cancel()
                for (status in results.statuses) if (PERMISSION_GRANTED != status) {
                    notGranted?.invoke()
                    return@listen
                }
                onGranted?.invoke()
            }
        })
    } else onGranted?.invoke()
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

