package renetik.android.sample.view.maps

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.textView
import renetik.android.maps.CSMapClientController
import renetik.android.maps.CSMapController
import renetik.android.maps.extensions.location
import renetik.android.maps.extensions.locationClient
import renetik.android.sample.R
import renetik.android.sample.model.MapMarker
import renetik.android.sample.model.model
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.text

@SuppressLint("MissingPermission")
class SampleMapMarkersController(title: String, mapController: CSMapController) :
    CSViewController<View>(navigation, layout(R.layout.sample_map_markers)) {

    private val mapClient = CSMapClientController(this, R.id.SampleMap_Map, mapController)

    init {
        textView(R.id.SampleMap_Title).text(title)
        mapClient.onMapShowing { map ->
            map.isMyLocationEnabled = true
            locationClient().location { mapController.camera(it) }
            loadMarkers()
        }
        mapClient.onMapClick { addMapClick(it) }
    }

    private fun loadMarkers() = model.mapMarkers.forEach { showMarker(it) }

    private fun showMarker(marker: MapMarker) =
        mapClient.map!!.addMarker(MarkerOptions().position(marker.latLng).title(marker.title))

    private fun addMapClick(latLng: LatLng) = dialog("Add marker at position")
        .showInput("Enter marker title") {
            showMarker(model.mapMarkers.add(MapMarker(latLng, it.inputText)))
            model.save()
        }
}


