package renetik.android.sample.model

import com.google.android.gms.maps.model.LatLng
import renetik.android.java.collections.list
import renetik.android.java.extensions.format
import java.io.File
import java.text.DateFormat
import java.util.*

class SampleModel {
    val sampleList = list(
            SampleListRow("title 1", "sub title 1"), SampleListRow("title 2", "sub title 2"),
            SampleListRow("title 3", "sub title 3"), SampleListRow("title 4", "sub title 4"))
    val sampleGetPictureList = list<SampleGetPictureRow>()
    val mapMarkers = list<MapMarker>()
    val mapRoute = list<LatLng>()
    val server by lazy { SampleServer() }
}

class SampleListRow(val title: String, val subtitle: String) {
    val time = Date().format(DateFormat.LONG, DateFormat.SHORT)
    val searchableText = "$title $subtitle"
}

class SampleGetPictureRow(val image: File)

class MapMarker(val latLng: LatLng, val title: String)
