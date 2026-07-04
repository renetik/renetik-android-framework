package renetik.android.json

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.obj.load

@RunWith(RobolectricTestRunner::class)
class TestSimpleJsonObjectTest {
    private val exceptedJson: String = """{
  "listKey": [
    1.2,
    3.4,
    5
  ],
  "mapKey": {
    "key1": true,
    "key2": false
  },
  "stringKey": "testObject"
}"""

    @Test
    fun customJsonObjectSetGetTest2() {
        val instance = TestSimpleJsonObject("testObject",
            mapOf("key1" to true, "key2" to false),
            listOf(1.2, 3.4, 5.0))
        assertEquals(exceptedJson, instance.toJson(formatted = true))
        assertEquals(instance, TestSimpleJsonObject().load(exceptedJson))
    }
}
