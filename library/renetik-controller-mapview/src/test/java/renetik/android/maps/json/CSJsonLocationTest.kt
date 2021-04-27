package renetik.android.maps.json

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.BuildConfig.DEBUG
import renetik.android.json.data.CSJsonMap
import renetik.android.json.data.properties.CSJsonString
import renetik.android.json.parseJson
import renetik.android.json.toJsonString
import renetik.android.logging.AndroidLogger
import renetik.android.framework.CSApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = ApplicationMock::class)
class CSJsonLocationTest {

    private val json = """ {"location":[90,-138],"title":"Nice House"} """

    @Test
    fun testJsonDataSave() {
        val house = HouseJsonDataTest()
        house.title.string = "Nice House"
        house.location.latLng = LatLng(222.0, 222.0)

        val loadedHouse = HouseJsonDataTest()
        loadedHouse.load(house.toJsonString().parseJson()!!)
        assertEquals(LatLng(222.0, 222.0), loadedHouse.location.latLng)
    }

    @Test
    fun testJsonDataLoad() {
        val house = HouseJsonDataTest()
        house.load(json.parseJson()!!)
        assertEquals(LatLng(90.0, -138.0), house.location.latLng)
    }
}

class HouseJsonDataTest : CSJsonMap() {
    val title = CSJsonString(this, "title")
    val location = CSJsonLocation(this, "location")
}

class ApplicationMock : CSApplication() {
    override val name = "ApplicationMock"
    override val logger by lazy { AndroidLogger() }
    override val isDebugBuild get() = DEBUG
}
