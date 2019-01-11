package renetik.android.json

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.BuildConfig.DEBUG
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.properties.CSJsonDataListProperty
import renetik.android.json.data.properties.CSJsonString
import renetik.android.base.CSApplication
import renetik.android.java.collections.list
import renetik.android.java.collections.map
import renetik.android.java.extensions.collections.secondItem
import renetik.android.java.extensions.primitives.trimNewLines

@RunWith(RobolectricTestRunner::class)
@Config(application = ApplicationMock::class)
class CSJsonTest {

    private val mapTest1 = map("id", 1, "isTrue", true, "list", list("litItem1", "litItem2", "litItem3"))
    private val jsonTest1 = """{"isTrue":true,"id":1,"list":["litItem1","litItem2","litItem3"]}"""

    @Test
    fun testToJson1() {
        assertEquals(jsonTest1, toJson(mapTest1))
    }

    @Test
    fun testFromJson1() {
        val map = fromJson<Map<*, *>>(jsonTest1)!!
        assertEquals("litItem2", (map["list"] as? List<*>)?.get(1))
    }

    private val mapTest2 = map("id", 1, "isTrue", true, 1, list("litItem1", Object(), "litItem3"))
    private val jsonTest2 = """{"isTrue":true,"1":["litItem1","java.lang.Object@"""

    @Test
    fun testToJson2() {
        assertTrue(toJson(mapTest2).startsWith(jsonTest2))
    }

    private val json =
            """{"floors":[{"title":"first"},{"title":"second"}],
                |"title":"Nice House"}""".trimMargin().trimNewLines()

    @Test
    fun testJsonDataSave() {
        val house = HouseJsonDataTest()
        house.floors.add(FlorJsonDataTest("first"))
        house.floors.add(FlorJsonDataTest("second"))
        house.title.string = "Nice House"
        house.load(fromJson(toJson(house))!!)
        assertEquals("second", house.floors.list.secondItem!!.title.value)
    }

    @Test
    fun testJsonDataLoad() {
        val house = HouseJsonDataTest()
        house.load(fromJson(json)!!)
        assertEquals("second", house.floors.list.secondItem!!.title.string)
    }

    @Test
    fun jsonDataListSave() {
        val house = HouseJsonDataTest()
        house.load(fromJson(json)!!)
        assertEquals(2, house.floors.size)
        house.floors.list = list(FlorJsonDataTest("first"),
                FlorJsonDataTest("second"), FlorJsonDataTest("third"))
        assertEquals(3, house.floors.size)
        assertEquals("third", house.floors.last!!.title.value)
        house.floors.clear()
        assertTrue(house.floors.isEmpty)
    }
}

class ApplicationMock : CSApplication() {
    override val isDebugBuild get() = DEBUG
}

class HouseJsonDataTest : CSJsonData() {
    val floors = CSJsonDataListProperty(this, FlorJsonDataTest::class, "floors")
    val title = CSJsonString(this, "title")
}

class FlorJsonDataTest() : CSJsonData() {

    val title = CSJsonString(this, "title")

    constructor(title: String) : this() {
        this.title.string = title
    }

}
