package renetik.android.json

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JsonEdgeCasesTest {

    @Before
    fun prepare() {
        CSJson.forceString = false
    }

    @Test
    fun deepNestingRoundTrip() {
        val nested = mapOf("level1" to mapOf("level2" to mapOf("level3" to
                listOf(mapOf("deep" to "value")))))
        val json = nested.toJson()
        val parsed = json.parseJsonMap()!!
        @Suppress("UNCHECKED_CAST")
        val level3 = ((parsed["level1"] as Map<String, Any?>)["level2"]
                as Map<String, Any?>)["level3"] as List<Map<String, Any?>>
        assertEquals("value", level3.first()["deep"])
    }

    @Test
    fun nullValuesInMapsAndLists() {
        val json = """{"present":"x","missing":null,"list":[1,null,3]}"""
        val parsed = json.parseJsonMap()!!
        assertEquals("x", parsed["present"])
        assertNull(parsed["missing"])
        val list = parsed["list"] as List<*>
        assertEquals(3, list.size)
        assertNull(list[1])
    }

    @Test
    fun specialCharactersSurviveRoundTrip() {
        val text = "quote:\" backslash:\\ newline:\n tab:\t unicode:ěščřžýáíé 絵文字 🎹"
        val json = mapOf("key" to text).toJson()
        assertEquals(text, json.parseJsonMap()!!["key"])
    }

    @Test
    fun numbersPrecision() {
        val json = """{"int":2147483647,"long":9007199254740991,"double":3.141592653589793}"""
        val parsed = json.parseJsonMap()!!
        assertEquals(2147483647, (parsed["int"] as Number).toInt())
        assertEquals(9007199254740991L, (parsed["long"] as Number).toLong())
        assertEquals(3.141592653589793, (parsed["double"] as Number).toDouble(), 0.0)
    }

    @Test
    fun emptyCollections() {
        assertEquals("{}", mapOf<String, Any?>().toJson())
        assertEquals("[]", listOf<Any?>().toJson())
        assertEquals(0, "{}".parseJsonMap()!!.size)
        assertEquals(0, "[]".parseJsonList()!!.size)
    }

    @Test
    fun emptyAndBlankStringsParseToNull() {
        assertNull("".parseJsonMap())
        assertNull("not a json".parseJsonMap())
    }

    @Test
    fun listOfMixedTypes() {
        val json = """["text",7,1.5,true,null]"""
        val list = json.parseJsonList()!!
        assertEquals("text", list[0])
        assertEquals(7, (list[1] as Number).toInt())
        assertEquals(1.5, (list[2] as Number).toDouble(), 0.0)
        assertEquals(true, list[3])
        assertNull(list[4])
    }
}
