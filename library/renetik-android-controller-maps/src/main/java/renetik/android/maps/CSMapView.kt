package renetik.android.maps

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import renetik.android.controller.base.CSActivityView
import renetik.android.core.kotlin.unfinished
import renetik.android.core.logging.CSLog.logError
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.listen
import renetik.android.maps.extensions.asLatLng

private const val DEFAULT_ZOOM = 13f

open class CSMapView(parent: CSActivityView<*>, private val options: GoogleMapOptions) :
    CSActivityView<MapView>(parent) {

    var map: GoogleMap? = null
    private val onMapReadyEvent = event<GoogleMap>()
    private var animatingCamera = false
    private var onCameraMoveStartedByUser = event<GoogleMap>()
    fun onCameraMoveStartedByUser(function: (GoogleMap) -> Unit) =
        onCameraMoveStartedByUser.listen(function)

    var onCameraStopped = event<GoogleMap>()
    fun onCameraStopped(function: (GoogleMap) -> Unit) = onCameraStopped.listen(function)
    private var onInfoWindowClick = event<Marker>()
    fun onMarkerInfoClick(function: (Marker) -> Unit) = onInfoWindowClick.listen(function)

    override fun createView() = MapView(this, options)

    constructor(parent: CSActivityView<*>) : this(parent, GoogleMapOptions())

//    override fun onCreate(bundle: Bundle?) {
//        super.onCreate(bundle)
//        catchApiKeyNotFound {
//            view.onCreate(bundle)
//        }
//    }

    private fun catchApiKeyNotFound(function: () -> Unit) {
        try {
            function()
        } catch (ex: RuntimeException) {
            logError { message(ex) }
            if (ex.message?.contains("API key not found") == true) unfinished()
            //TODO finish if CSMapView is used ever
//                dialog("Error", ex.message!!).cancelable(false).show {
//                    exitProcess(1)
//                }
            else throw ex
        }
    }

//    override fun onResume() {
//        super.onResume()
//        catchApiKeyNotFound {
//            view.onResume()
//            view.getMapAsync { onInitializeMap(it) }
//        }
//    }

//    override fun onPause() {
//        super.onPause()
//        catchApiKeyNotFound {
//            view.onPause()
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        catchApiKeyNotFound {
//            view.onStart()
//        }
//    }

//    override fun onStop() {
//        super.onStop()
//        catchApiKeyNotFound {
//            view.onStop()
//        }
//    }

//    override fun onDestroy() {
//        catchApiKeyNotFound {
//            view.onDestroy()
//        }
//        super.onDestroy()
//    }

//    override fun onLowMemory() {
//        super.onLowMemory()
//        catchApiKeyNotFound {
//            view.onLowMemory()
//        }
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        catchApiKeyNotFound {
//            view.onSaveInstanceState(outState)
//        }
//    }

    private fun onInitializeMap(map: GoogleMap) {
        this.map = map
        onMapReadyEvent.fire(map)
        map.setOnCameraMoveStartedListener { onCameraMoveStarted() }
        map.setOnCameraIdleListener { onCameraMoveStopped() }
        map.setOnCameraMoveCanceledListener { onCameraMoveStopped() }
        map.setOnInfoWindowClickListener { onInfoWindowClick.fire(it) }
    }

    fun camera(location: Location, zoom: Float) = camera(location.asLatLng(), zoom)

    fun camera(latLng: LatLng) = camera(latLng, DEFAULT_ZOOM)

    fun camera(location: Location) = camera(location.asLatLng())

    fun camera(latLng: LatLng, zoom: Float) {
        animatingCamera = true
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
        animatingCamera = true
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
        animatingCamera = false
    }

    fun onMapAvailable(parent: CSActivityView<*>, onMapReady: (GoogleMap) -> Unit) {
        map?.let { onMapReady(it) } ?: let {
            parent.whileShowing(onMapReadyEvent.listen { registration, map ->
                whileShowingCancel(registration)
                onMapReady(map)
            })
        }
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
        map?.setOnMapClickListener(null)
    }


}