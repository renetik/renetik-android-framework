package renetik.android.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.net.Uri.parse
import android.os.Build.VERSION.SDK_INT
import android.os.Environment.getExternalStorageDirectory
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng
import renetik.android.extensions.view.dialog
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.java.lang.CSMath.randomInt
import renetik.android.lang.CSLang.*
import renetik.android.rpc.CSRequest
import renetik.android.viewbase.CSViewController
import java.io.File

fun <T : CSViewController<*>> T.navigateToLatLng(latLng: LatLng, title: String) {
    val uri = stringf("http://maps.google.com/maps?&daddr=%f,%f (%s)",
            latLng.latitude, latLng.longitude, title)
    try {
        startActivity(Intent(ACTION_VIEW, parse(uri)).apply {
            setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        })
    } catch (ex: ActivityNotFoundException) {
        try {
            startActivity(Intent(ACTION_VIEW, parse(uri)))
        } catch (e: ActivityNotFoundException) {
            error(e)
        }
    }
}

private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

fun <T : CSViewController<*>> T.checkPlayServices() {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val result = apiAvailability.isGooglePlayServicesAvailable(this)
    if (result != ConnectionResult.SUCCESS) {
        if (apiAvailability.isUserResolvableError(result))
            apiAvailability.getErrorDialog(activity(), result, PLAY_SERVICES_RESOLUTION_REQUEST).show()
        else
            dialog("Google Play Services missing application cannot continue").show { activity().finish() }
    }
}

fun <T : CSViewController<*>> T.sendMail(email: String, subject: String, text: String) {
    sendMail(list(email), subject, text, list())
}

fun <T : CSViewController<*>> T.sendMail(emails: CSList<String>, subject: String, body: String,
                                         attachment: File) {
    sendMail(emails, subject, body, list(attachment))
}

fun <T : CSViewController<*>> T.sendMail(emails: CSList<String>, subject: String, body: String,
                                         attachments: List<File>) {
    Intent(if (attachments.isEmpty()) ACTION_SEND else ACTION_SEND_MULTIPLE).apply {
        putExtra(EXTRA_EMAIL, emails.toTypedArray()).putExtra(EXTRA_SUBJECT, subject)
        putExtra(EXTRA_TEXT, body).type = "text/plain"
        val attachmentUris = ArrayList<Uri>()
        attachments.forEach { file ->
            if (!file.startsWith(getExternalStorageDirectory()))
                throw Exception("Attachment not in ExternalStorageDirectory")
            else if (!(file.exists() && file.canRead())) throw Exception("Attachment can not be read")
            attachmentUris.add(Uri.fromFile(file))
        }
        if (attachments.isNotEmpty()) putParcelableArrayListExtra(EXTRA_STREAM, attachmentUris);
        startActivity(createChooser(this, "Pick an Email provider"))
    }
}

fun <Data : Any> CSViewController<*>.sendRequest(title: String, request: CSRequest<Data>): CSRequest<Data> {
    val progress = dialog(title).showIndeterminateProgress {
        request.cancel()
    }
    request.send().onDone {
        progress.hide()
    }.onFailed {
        dialog(title, "Operation failed").show("Retry", {
            sendRequest(title, request)
        }, "Ok", {
            request.cancel()
        })
    }
    return request
}

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
        if (isPermissionGranted(permission))
            deniedPermissions.add(permission)
    return toStringArray(deniedPermissions)
}

fun CSViewController<*>.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED
}

