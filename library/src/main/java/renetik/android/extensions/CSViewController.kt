package renetik.android.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.net.Uri.parse
import android.os.Environment.getExternalStorageDirectory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng
import renetik.android.extensions.view.dialog
import renetik.android.rpc.CSResponse
import renetik.android.viewbase.CSViewController
import renetik.android.java.collections.CSList
import renetik.android.lang.CSLang.*
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
    val result = apiAvailability.isGooglePlayServicesAvailable(context())
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

fun CSViewController<*>.showResponse(title: String, response: CSResponse<*>): CSResponse<out Any> {
    val dialog = dialog(title).showIndeterminateProgress { response.cancel() }
    return response.onFailed { dialog(title, "Operation failed").show() }.onDone { dialog.hide() }
}

