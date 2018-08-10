package cs.android.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import com.google.android.gms.maps.model.LatLng
import cs.android.viewbase.CSViewController
import cs.java.lang.CSLang.stringf

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
