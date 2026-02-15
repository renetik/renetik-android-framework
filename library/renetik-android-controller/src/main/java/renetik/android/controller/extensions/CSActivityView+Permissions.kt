package renetik.android.controller.extensions

import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.requestPermissions

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
    permissions: List<String>, onGranted: (() -> Unit)? = null,
    onNotGranted: (() -> Unit)? = null, onDone: (() -> Unit)? = null,
) = activity().requestPermissions(permissions, onGranted, onNotGranted, onDone)