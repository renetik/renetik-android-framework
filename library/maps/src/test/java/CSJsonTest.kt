package renetik.android.maps.json

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.BuildConfig.DEBUG
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonStringProperty
import renetik.android.json.fromJson
import renetik.android.json.toJson
import renetik.android.logging.AndroidLogger
import renetik.android.base.CSApplication
import renetik.java.extensions.primitives.trimNewLines

@RunWith(RobolectricTestRunner::class)
@Config(application = ApplicationMock::class)
class CSJsonTest {

    private val json =
            """"location":[90,-138],"title":"Nice House"}""".trimMargin().trimNewLines()

    @Test
    fun testJsonDataSave() {
        val house = HouseJsonDataTest()
        house.title.string = "Nice House"
        house.location.latLng = LatLng(222.0, 222.0)

        val loadedHouse = HouseJsonDataTest()
        loadedHouse.load(fromJson(toJson(house))!!)
        assertEquals(LatLng(222.0, 222.0), loadedHouse.location.latLng)
    }

    @Test
    fun testJsonDataLoad() {
        val house = HouseJsonDataTest()
        house.load(fromJson(json)!!)
        assertEquals(LatLng(90.0, -138.0), house.location.latLng)
    }
}

class HouseJsonDataTest : CSJsonData() {
    val title = CSJsonStringProperty(this, "title")
    val location = CSJsonLocationProperty(this, "location")
}

class ApplicationMock : CSApplication() {
    override val name = "ApplicationMock"
    override val logger by lazy { AndroidLogger() }
    override val isDebugBuild get() = DEBUG
}
