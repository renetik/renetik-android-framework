package cs.android.json

import com.google.android.gms.maps.model.LatLng
import cs.android.BuildConfig
import cs.java.lang.CSLang.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class) @Config(constants = BuildConfig::class) class CSJsonTest {

    val mapTest1 = map("id", 1, "isTrue", YES, "list", list("litItem1", "litItem2", "litItem3"))
    val jsonTest1 = """{"isTrue":true,"id":1,"list":["litItem1","litItem2","litItem3"]}"""

    @Test fun testToJson1() {
        assertEquals(jsonTest1, toJson(mapTest1))
    }

    @Test fun testFromJson1() {
        val map = fromJson(jsonTest1) as Map<*, *>
        assertEquals("litItem2", (map["list"] as? List<*>)?.get(1))
    }

    val mapTest2 = map("id", 1, "isTrue", YES, 1, list("litItem1", Object(), "litItem3"))
    val jsonTest2 = """{"isTrue":true,"1":["litItem1","java.lang.Object@"""

    @Test fun testToJson2() {
        assertTrue(toJson(mapTest2).startsWith(jsonTest2))
    }

    val json =
            """{"images":["path1","path2"],"floors":[{"title":"first"},{"title":"second"}],"location":[90,-138],"title":"Nice House"}"""

    @Test fun testJsonDataSave() {
        val house = HouseJsonDataTest()
        house.floors.add(FlorJsonDataTest("first"))
        house.floors.add(FlorJsonDataTest("second"))
        house.title.value = "Nice House"
        house.images.add(File("path1"))
        house.images.add(File("path2"))
        house.location.value = LatLng(222.0, 222.0)
        assertEquals(json, toJson(house))
    }

    @Test fun testJsonDataLoad() {
        val house = HouseJsonDataTest()
        house.load(fromJson(json) as Map<String, Any?>)
        assertEquals("second", house.floors.value.second().title.it)
        assertEquals(File("path1"), house.images.value.first())
        assertEquals(LatLng(222.0, 222.0), house.location.value)
    }

}

class HouseJsonDataTest : CSJsonData() {
    val floors = CSJsonListProperty(this, FlorJsonDataTest::class, "floors")
    val title = CSJsonStringProperty(this, "title")
    val images = CSJsonFileListProperty(this, "images")
    val location = CSJsonLocationProperty(this, "location")
}

class FlorJsonDataTest() : CSJsonData() {

    val title = CSJsonStringProperty(this, "title")

    constructor(title: String) : this() {
        this.title.value = title
    }

}
