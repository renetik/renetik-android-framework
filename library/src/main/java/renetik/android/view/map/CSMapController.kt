package renetik.android.view.map

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import renetik.android.java.event.CSEvent
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.event.event
import renetik.android.java.event.execute
import renetik.android.lang.CSLang.NO
import renetik.android.lang.CSLang.YES
import renetik.android.location.asLatLng
import renetik.android.viewbase.CSViewController

open class CSMapController(parent: CSViewController<*>, val options: GoogleMapOptions) : CSViewController<MapView>(parent) {

    var map: GoogleMap? = null
    private val onMapReadyEvent: CSEvent<GoogleMap> = event()
    private var animatingCamera = NO
    private var onCameraMoveStartedByUser = event<GoogleMap>()
    fun onCameraMoveStartedByUser(function: (GoogleMap) -> Unit) = onCameraMoveStartedByUser.execute(function)
    var onCameraStopped = event<GoogleMap>()
    fun onCameraStopped(function: (GoogleMap) -> Unit) = onCameraStopped.execute(function)
    private var onInfoWindowClick = event<Marker>()
    fun onMarkerInfoClick(function: (Marker) -> Unit) = onInfoWindowClick.execute(function)

    override fun createView() = MapView(this, options)

    constructor(parent: CSViewController<*>) : this(parent, GoogleMapOptions())

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        view.onCreate(state)
    }

    override fun onResume() {
        super.onResume()
        view.onResume()
        view.getMapAsync { onInitializeMap(it) }
    }

    override fun onPause() {
        super.onPause()
        view.onPause()
    }

    override fun onStart() {
        super.onStart()
        view.onStart()
    }

    override fun onStop() {
        super.onStop()
        view.onStop()
    }

    override fun onDestroy() {
        view.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view.onSaveInstanceState(outState)
    }

    private fun onInitializeMap(map: GoogleMap) {
        this.map = map
        onMapReadyEvent.fire(map)
        map.setOnCameraMoveStartedListener { onCameraMoveStarted() }
        map.setOnCameraIdleListener { onCameraMoveStopped() }
        map.setOnCameraMoveCanceledListener { onCameraMoveStopped() }
        map.setOnInfoWindowClickListener { onInfoWindowClick.fire(it) }
    }

    fun camera(location: Location, zoom: Float) = camera(location.asLatLng(), zoom)

    fun camera(latLng: LatLng, zoom: Float) {
        animatingCamera = YES
        map?.animateCamera(newLatLngZoom(latLng, zoom), object : GoogleMap.CancelableCallback {
            override fun onCancel() {
                onAnimateCameraDone()
            }

            override fun onFinish() {
                onAnimateCameraDone()
            }
        })
    }

    fun camera(latLng: LatLng, zoom: Float, onFinished: () -> Unit) {
        animatingCamera = YES
        map?.animateCamera(newLatLngZoom(latLng, zoom), object : GoogleMap.CancelableCallback {
            override fun onCancel() {
                onAnimateCameraDone()
                onFinished()
            }

            override fun onFinish() {
                onAnimateCameraDone()
                onFinished()
            }
        })
    }

    private fun onAnimateCameraDone() {
        animatingCamera = NO
    }

    fun onMapAvailable(onMapReady: (GoogleMap) -> Unit): CSEventRegistration? {
        map?.let { onMapReady(it) } ?: let {
            return onMapReadyEvent.run { registration, map ->
                onMapReady(map)
                registration.cancel()
            }
        }
        return null
    }

    private fun onCameraMoveStarted() {
        if (animatingCamera) return
        onCameraMoveStartedByUser.fire(map!!)
    }

    private fun onCameraMoveStopped() {
        onCameraStopped.fire(map!!)
    }

    fun clearMap() {
        map?.clear()
        map?.setOnMapLongClickListener(null)
    }


}