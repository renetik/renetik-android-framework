package renetik.android.sample.model

import com.google.android.gms.maps.model.LatLng
import renetik.android.base.CSApplication.Companion.application
import renetik.android.java.extensions.format
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.properties.CSJsonDataList
import renetik.android.json.data.properties.CSJsonFile
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.extensions.save
import renetik.android.maps.json.CSJsonLocation
import java.io.File
import java.text.DateFormat
import java.util.*

const val MODEL_KEY = "model_data"

class SampleModel : CSJsonMap() {

    val sampleList = CSJsonDataList(this, ListItem::class, "sampleList")
    val getPictureList = CSJsonDataList(this, ImageItem::class, "getPictureList")
    val mapMarkers = CSJsonDataList(this, MapMarker::class, "mapMarkers")
    val mapRoute = CSJsonDataList(this, MapPosition::class, "mapRoute")
    val server by lazy { SampleServer() }

    fun save() {
        application.store.save(MODEL_KEY, this)
    }

    init {
        if (sampleList.isEmpty)
            sampleList.add(ListItem("title 1", "sub title 1"), ListItem("title 2", "sub title 2"),
                    ListItem("title 3", "sub title 3"), ListItem("title 4", "sub title 4"))
    }
}

class ListItem() : CSJsonMap() {
    val searchableText get() = "$title $subtitle"
    val time = CSJsonString(this, "time")
    val title = CSJsonString(this, "title")
    val subtitle = CSJsonString(this, "subtitle")

    constructor(title: String, subtitle: String) : this() {
        this.title.string = title
        this.subtitle.string = subtitle
        time.string = Date().format(DateFormat.LONG, DateFormat.SHORT)
    }
}

class ImageItem() : CSJsonMap() {
    val image = CSJsonFile(this, "image")

    constructor(image: File) : this() {
        this.image.file = image
    }
}

class MapMarker() : CSJsonMap() {
    val latLng get() = locationProperty.value
    val title get() = titleProperty.value
    private val locationProperty = CSJsonLocation(this, "position")
    private val titleProperty = CSJsonString(this, "title")

    constructor(latLng: LatLng, title: String) : this() {
        locationProperty.latLng = latLng
        titleProperty.string = title
    }
}

class MapPosition() : CSJsonMap() {
    val latLng get() = property.latLng!!
    private val property = CSJsonLocation(this, "position")

    constructor(latLng: LatLng) : this() {
        property.latLng = latLng
    }
}

