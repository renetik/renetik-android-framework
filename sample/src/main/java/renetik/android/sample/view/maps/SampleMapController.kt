package renetik.android.sample.view.maps

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.GoogleMap
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.button
import renetik.android.controller.extensions.floatingButton
import renetik.android.controller.extensions.textView
import renetik.android.maps.CSMapClientController
import renetik.android.maps.CSMapController
import renetik.android.sample.R
import renetik.android.sample.view.navigation
import renetik.android.sample.view.push
import renetik.android.view.extensions.image
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.text
import renetik.android.view.extensions.title

class SampleMapController(title: String) :
    CSViewController<View>(navigation, layout(R.layout.sample_map)),
    CSNavigationItem {

    private val mapController = CSMapController(this)
    private val mapTypeButton =
        floatingButton(R.id.SampleMap_MapTypeButton).onClick { onMapTypeClick() }

    init {
        textView(R.id.SampleMap_Title).text(title)
        CSMapClientController(this, R.id.SampleMap_Map, mapController)
        button(R.id.SampleMap_DrawPathMapButton)
            .onClick { SampleMapPathController(it.title, mapController).push() }
        button(R.id.SampleMap_MarkerMapButton)
            .onClick { SampleMapMarkersController(it.title, mapController).push() }
    }

    @SuppressLint("MissingPermission")
    override fun onViewShowing() {
        super.onViewShowing()
        whileShowing(mapController.onMapAvailable { map ->
            mapTypeNormal(map)
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        })
    }

    private fun onMapTypeClick() {
        mapController.map?.let { map ->
            if (map.mapType == GoogleMap.MAP_TYPE_SATELLITE) mapTypeNormal(map)
            else mapTypeSatellite(map)
        }
    }

    private fun mapTypeSatellite(map: GoogleMap) {
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mapTypeButton.image(R.drawable.outline_satellite_24)
    }

    private fun mapTypeNormal(map: GoogleMap) {
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        mapTypeButton.image(R.drawable.outline_map_24)
    }

}