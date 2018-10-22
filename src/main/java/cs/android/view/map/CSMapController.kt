package cs.android.view.map

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import cs.android.viewbase.CSViewController
import cs.java.event.CSEvent
import cs.java.event.CSEvent.CSEventRegistration
import cs.java.lang.CSLang.*

open class CSMapController(parent: CSViewController<*>, val options: GoogleMapOptions) : CSViewController<MapView>(parent, null) {

    constructor(parent: CSViewController<*>) : this(parent, GoogleMapOptions())

    var map: GoogleMap? = null
    private val onMapReadyEvent: CSEvent<GoogleMap> = event()
    private var animatingCamera = NO
    var onCameraMoveStartedByUser = event<GoogleMap>()
    var onCameraMoveStopped = event<GoogleMap>()

    init {
        view = MapView(this.context(), options)
    }

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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        view.onSaveInstanceState(outState)
    }

    private fun onInitializeMap(map: GoogleMap) {
        this.map = map
        onMapReadyEvent.fire(map)
        map.setOnCameraMoveStartedListener { onCameraMoveStarted() }
        map.setOnCameraIdleListener { onCameraMoveStopped() }
        map.setOnCameraMoveCanceledListener { onCameraMoveStopped() }
    }

    fun animateCamera(latLng: LatLng, zoom: Float) {
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

    fun animateCamera(latLng: LatLng, zoom: Float, onFinished: () -> Unit) {
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
            return onMapReadyEvent.add { registration, map ->
                onMapReady(map)
                registration.cancel()
            }
        }
        return null
    }

    private fun onCameraMoveStarted() {
        info("onCameraMoveStarted")
        if (animatingCamera) return
        info("onCameraMoveStartedByUser")
        onCameraMoveStartedByUser.fire(map)
    }

    private fun onCameraMoveStopped() {
        info("onCameraMoveStopped")
        onCameraMoveStopped.fire(map)
    }

    fun clearMap() {
        map?.clear()
        map?.setOnMapLongClickListener(null)
        map?.setOnInfoWindowClickListener(null)
    }

}