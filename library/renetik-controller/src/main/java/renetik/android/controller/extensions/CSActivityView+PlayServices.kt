package renetik.android.controller.extensions

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import renetik.android.controller.base.CSActivityView

private const val RESOLUTION_REQUEST = 9000

//This is not really perfect code...
fun <T : CSActivityView<*>> T.checkPlayServices() {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val result = apiAvailability.isGooglePlayServicesAvailable(this)
    if (result != ConnectionResult.SUCCESS) {
        if (apiAvailability.isUserResolvableError(result))
            apiAvailability.getErrorDialog(activity(), result, RESOLUTION_REQUEST)?.show()
                ?: dialog("Google Play Services missing application cannot continue").show {
                    activity().finish()
                }
        else dialog("Google Play Services missing application cannot continue").show {
            activity().finish()
        }
    }
}