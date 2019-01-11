package renetik.android.sample.model

import com.google.android.gms.maps.model.LatLng
import renetik.android.base.application
import renetik.android.java.extensions.format
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonDataListProperty
import renetik.android.json.data.properties.CSJsonFileProperty
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.extensions.save
import renetik.android.maps.json.CSJsonLocationProperty
import java.io.File
import java.text.DateFormat
import java.util.*

const val MODEL_KEY = "model_data"

class SampleModel : CSJsonData() {

    val sampleList = CSJsonDataListProperty(this, ListItem::class, "sampleList")
    val getPictureList = CSJsonDataListProperty(this, ImageItem::class, "getPictureList")
    val mapMarkers = CSJsonDataListProperty(this, MapMarker::class, "mapMarkers")
    val mapRoute = CSJsonDataListProperty(this, MapPosition::class, "mapRoute")
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

class ListItem() : CSJsonData() {
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

class ImageItem() : CSJsonData() {
    val image = CSJsonFileProperty(this, "image")

    constructor(image: File) : this() {
        this.image.file = image
    }
}

class MapMarker() : CSJsonData() {
    val latLng get() = locationProperty.value
    val title get() = titleProperty.value
    private val locationProperty = CSJsonLocationProperty(this, "position")
    private val titleProperty = CSJsonString(this, "title")

    constructor(latLng: LatLng, title: String) : this() {
        locationProperty.latLng = latLng
        titleProperty.string = title
    }
}

class MapPosition() : CSJsonData() {
    val latLng get() = property.latLng!!
    private val property = CSJsonLocationProperty(this, "position")

    constructor(latLng: LatLng) : this() {
        property.latLng = latLng
    }
}

