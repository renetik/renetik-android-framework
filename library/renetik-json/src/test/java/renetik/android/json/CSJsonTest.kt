package renetik.android.json

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.BuildConfig.DEBUG
import renetik.android.base.CSApplication
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.second
import renetik.android.java.extensions.primitives.trimNewLines
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.clone
import renetik.android.json.data.properties.CSJsonDataList
import renetik.android.json.data.properties.CSJsonString

@RunWith(RobolectricTestRunner::class)
@Config(application = ApplicationMock::class)
class CSJsonTest {

    private val mapTest1 =
        mapOf("id" to 1, "isTrue" to true, "list" to list("litItem1", "litItem2", "litItem3"))
    private val jsonTest1 = """{"isTrue":true,"id":1,"list":["litItem1","litItem2","litItem3"]}"""

    @Test
    fun testToJson1() {
        assertEquals(jsonTest1, mapTest1.toJsonString())
    }

    @Test
    fun testFromJson1() {
        val map = jsonTest1.parseJson<Map<*, *>>()!!
        assertEquals("litItem2", (map["list"] as? List<*>)?.get(1))
    }

    private val mapTest2 =
        mapOf("id" to 1, "isTrue" to true, 1 to list("litItem1", Object(), "litItem3"))
    private val jsonTest2 = """{"isTrue":true,"1":["litItem1","java.lang.Object@"""

    @Test
    fun testToJson2() {
        assertTrue(mapTest2.toJsonString().startsWith(jsonTest2))
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
        house.load(house.toJsonString().parseJson()!!)
        assertEquals("second", house.floors.list.second!!.title.value)
    }

    @Test
    fun testJsonDataLoad() {
        val house = HouseJsonDataTest()
        house.load(json.parseJson()!!)
        assertEquals("second", house.floors.list.second!!.title.string)
    }

    @Test
    fun jsonDataListSave() {
        val house = HouseJsonDataTest()
        house.load(json.parseJson()!!)
        assertEquals(2, house.floors.size)
        house.floors.list = list(FlorJsonDataTest("first"),
            FlorJsonDataTest("second"), FlorJsonDataTest("third"))
        assertEquals(3, house.floors.size)
        assertEquals("third", house.floors.last!!.title.value)
        house.floors.clear()
        assertTrue(house.floors.isEmpty)
    }

    @Test
    fun clone() {
        val house = HouseJsonDataTest()
        house.floors.add(FlorJsonDataTest("first"))
        house.floors.add(FlorJsonDataTest("second"))

        val houseClone = house.clone()
        house.floors.last!!.title.value = "Original house last floor title"

        assertTrue(houseClone::class == house::class)
        assertEquals(house.floors.size, houseClone.floors.size)
        assertNotEquals(houseClone.floors.last!!.title.value, house.floors.last!!.title.value)
    }
}

class ApplicationMock : CSApplication() {
    override val isDebugBuild get() = DEBUG
}

class HouseJsonDataTest : CSJsonData() {
    val floors = CSJsonDataList(this, FlorJsonDataTest::class, "floors")
    val title = CSJsonString(this, "title")
}

class FlorJsonDataTest() : CSJsonData() {

    val title = CSJsonString(this, "title")

    constructor(title: String) : this() {
        this.title.string = title
    }

}
