package renetik.android.controller.extensions

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import renetik.android.controller.base.CSViewController
import renetik.android.dialog.extensions.dialog

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