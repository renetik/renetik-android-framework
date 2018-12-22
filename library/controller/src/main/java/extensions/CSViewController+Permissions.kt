package renetik.android.controller.extensions

import android.os.Build.VERSION.SDK_INT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import renetik.android.controller.base.CSViewController
import renetik.android.java.collections.list
import renetik.android.java.extensions.set
import renetik.android.java.math.CSMath.randomInt

fun CSViewController<*>.requestPermissions(permissions: List<String>, onGranted: () -> Unit) {
    requestPermissions(permissions, onGranted, null)
}

fun CSViewController<*>.requestPermissionsWithForce(permissions: List<String>, onGranted: () -> Unit) {
    requestPermissions(permissions, onGranted, { requestPermissionsWithForce(permissions, onGranted) })
}

fun CSViewController<*>.requestPermissions(permissions: List<String>,
                                           onGranted: () -> Unit, onNotGranted: (() -> Unit)?) {
    if (SDK_INT < 23) {
        onGranted()
        return
    }
    val deniedPermissions = getDeniedPermissions(permissions)
    if (set(deniedPermissions)) {
        val MY_PERMISSIONS_REQUEST = randomInt(0, 999)
        ActivityCompat.requestPermissions(activity(), deniedPermissions, MY_PERMISSIONS_REQUEST)
        onRequestPermissionsResult.run { registration, results ->
            if (results.requestCode == MY_PERMISSIONS_REQUEST) {
                registration.cancel()
                for (status in results.statuses) if (PERMISSION_GRANTED != status) {
                    onNotGranted?.invoke()
                    return@run
                }
                onGranted()
            }
        }
    } else onGranted()
}

fun CSViewController<*>.getDeniedPermissions(permissions: List<String>): Array<String> {
    val deniedPermissions = list<String>()
    for (permission in permissions)
        if (isPermissionGranted(permission)) deniedPermissions.add(permission)
    return deniedPermissions.toTypedArray()
}

fun CSViewController<*>.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED
}

