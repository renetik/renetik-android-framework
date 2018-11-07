package renetik.android.view.map

import android.view.View
import com.google.android.gms.maps.model.LatLng
import renetik.android.extensions.view.frame
import renetik.android.extensions.view.layoutMatchParent
import renetik.android.extensions.view.removeFromSuperview
import renetik.android.viewbase.CSViewController

open class CSMapClientController<V : View>(parent: CSViewController<V>, private val mapFrameId: Int,
                                           open val mapController: CSMapController) :
        CSViewController<V>(parent) {

    private var lastLocation: LatLng? = null
    private var lastZoom: Float? = null

    override fun onViewShowing() {
        super.onViewShowing()
        whileShowing(mapController.onMapAvailable { mapController.clearMap() })
        whileShowing(mapController.onCameraStopped { map ->
            lastLocation = map.cameraPosition.target
            lastZoom = map.cameraPosition.zoom
        })
        lastLocation?.let { latLng -> mapController.camera(latLng, lastZoom!!) }
        frame(mapFrameId).addView(mapController.view.removeFromSuperview(), layoutMatchParent())
    }

}


