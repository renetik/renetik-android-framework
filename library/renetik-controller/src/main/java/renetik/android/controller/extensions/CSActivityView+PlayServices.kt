package renetik.android.controller.extensions

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import renetik.android.controller.base.CSActivityView
import renetik.android.framework.lang.CSUserAction

private const val RESOLUTION_REQUEST = 9000

fun <T : CSActivityView<*>> T.checkPlayServices() {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val result = apiAvailability.isGooglePlayServicesAvailable(this)
    if (result != ConnectionResult.SUCCESS) {
        if (apiAvailability.isUserResolvableError(result))
            apiAvailability.getErrorDialog(activity(), result, RESOLUTION_REQUEST)?.show()
                ?: snackBarError("Google Play Services missing application cannot continue",
                    CSUserAction("Finish") { activity().finish() })
        else snackBarError("Google Play Services missing application cannot continue",
            CSUserAction("Finish") { activity().finish() })
    }
}