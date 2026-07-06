package renetik.android.core.android.content

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

fun Context.getDeniedPermissions(permissions: List<String>): Array<String> {
    val deniedPermissions = mutableListOf<String>()
    for (permission in permissions)
        if (!isPermissionGranted(permission)) deniedPermissions.add(permission)
    return deniedPermissions.toTypedArray()
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
}

fun Context.isPermissionsGranted(vararg permissions: String): Boolean =
    permissions.all { isPermissionGranted(it) }

fun Context.isPermissionsGranted(permissions: Iterable<String>): Boolean =
    permissions.all { isPermissionGranted(it) }
