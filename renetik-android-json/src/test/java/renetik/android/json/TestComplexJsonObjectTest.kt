package renetik.android.json

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.obj.load

@RunWith(RobolectricTestRunner::class)
class TestComplexJsonObjectTest {
    private val expectedJson = """
{
  "listKey": [
    {
      "stringKey": "listTestObject1"
    },
    {
      "stringKey": "listTestObject2"
    }
  ],
  "mapKey": {
    "mapKey1": {
      "stringKey": "mapTestObject1"
    },
    "mapKey2": {
      "stringKey": "mapTestObject2"
    }
  },
  "stringKey": "testObject"
}""".trimStart()

    @Test
    fun customJsonObjectSetGetTest2() {
        val instance = TestComplexJsonObject("testObject",
            mapOf("mapKey1" to TestComplexJsonObject("mapTestObject1"),
                "mapKey2" to TestComplexJsonObject("mapTestObject2")),
            listOf(TestComplexJsonObject("listTestObject1"),
                TestComplexJsonObject("listTestObject2")))

        Assert.assertEquals(expectedJson, instance.toJson(formatted = true))
        Assert.assertEquals(instance, TestComplexJsonObject().load(expectedJson))
    }
}
