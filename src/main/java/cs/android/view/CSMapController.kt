package cs.android.view

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import cs.android.viewbase.CSViewController
import cs.java.event.CSEvent
import cs.java.event.CSEvent.CSEventRegistration
import cs.java.lang.CSLang.*

class CSMapController(parent: CSViewController<*>, viewId: Int) :
        CSViewController<MapView>(parent, viewId) {

    var map: GoogleMap? = null
    private val onMapReadyEvent: CSEvent<GoogleMap> = event()
    private var animatingCamera = NO
    var onCameraMoveStartedByUser: (() -> Unit)? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        getView().onCreate(state)
        getView().getMapAsync { onInitializeMap(it) }
    }

    override fun onResume() {
        super.onResume()
        getView().onResume()
    }

    override fun onPause() {
        super.onPause()
        getView().onPause()
    }

    override fun onStart() {
        super.onStart()
        getView().onStart()
    }

    override fun onStop() {
        super.onStop()
        getView().onStop()
    }

    override fun onDestroy() {
        getView().onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        getView().onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        getView().onSaveInstanceState(outState)
    }

    private fun onInitializeMap(map: GoogleMap) {
        this.map = map
        onMapReadyEvent.fire(map)
        map.setOnCameraMoveStartedListener { onCameraMoveStarted() }
    }

    fun animateCamera(latLng: LatLng, zoom: Float) {
        animatingCamera = YES;
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), object : GoogleMap.CancelableCallback {
            override fun onCancel() {
                onAnimateCameraDone()
            }

            override fun onFinish() {
                onAnimateCameraDone()
            }
        })
    }

    fun animateCamera(latLng: LatLng, zoom: Float, onFinished: () -> Unit) {
        animatingCamera = YES;
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), object : GoogleMap.CancelableCallback {
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
                run {
                    onMapReady(map)
                    registration.cancel()
                }
            }
        }
        return null
    }

    private fun onCameraMoveStarted() {
        if (animatingCamera) return
        onCameraMoveStartedByUser?.invoke()
    }

}