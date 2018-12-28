package renetik.android.sample.view

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.title
import renetik.android.maps.CSMapClientController
import renetik.android.maps.CSMapController
import renetik.android.sample.R
import renetik.android.sample.model.MapMarker
import renetik.android.sample.model.model

@SuppressLint("MissingPermission")
class SampleMapMarkersController(title: String, mapController: CSMapController) : CSViewController<View>(navigation, layout(R.layout.sample_map)),
        CSNavigationItem {

    private val mapClient = CSMapClientController(this, R.id.SampleMap_Map, mapController)

    init {
        title(R.id.SampleMap_Title, title)
        mapClient.onMapShowing { map ->
            map.isMyLocationEnabled = true
            loadMarkers(map)
        }
        mapClient.onMapClick { addMapClick(it) }
    }

    private fun loadMarkers(map: GoogleMap) = model.mapMarkers.forEach { addMarker(map, it) }

    private fun addMarker(map: GoogleMap, marker: MapMarker) =
            map.addMarker(MarkerOptions().position(marker.latLng).title(marker.title))

    private fun addMapClick(latLng: LatLng) = dialog("Add marker at position")
            .showInput("Enter marker title") { model.mapMarkers.add(MapMarker(latLng, it.inputValue())) }
}


