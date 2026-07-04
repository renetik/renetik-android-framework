<!---Header--->
[![Android Build](https://github.com/renetik/renetik-android-json/workflows/Android%20Build/badge.svg)
](https://github.com/renetik/renetik-android-json/actions/workflows/android.yml)

# Renetik Android - Json

#### [https://github.com/renetik/renetik-android-json](https://github.com/renetik/renetik-android-json/) ➜ [Documentation](https://renetik.github.io/renetik-android-json/)

Framework to enjoy, improve and speed up your application development while writing readable code.
Used as library in many projects and improving it while developing new projects. I am open
for [Hire](https://renetik.github.io) or investment in my mobile app music production & perfromance
project Renetik Instruments www.renetik.com.

## Examples

```kotlin
data class SimpleJsonObject(
    var string: String? = null,
    var map: Map<String, Boolean>? = null,
    var list: List<Double>? = null) : CSJsonObject() {

    init {
        set("stringKey", string)
        set("mapKey", map)
        set("listKey", list)
    }

    override fun onLoaded() {
        string = get("stringKey")
        map = getBooleanMap("mapKey", default = false)
        list = getDoubleList("listKey", default = 0.0)
    }
}

@RunWith(RobolectricTestRunner::class)
class SimpleJsonObjectTest {
    private val exceptedJson: String = """{
  "stringKey": "testObject",
  "mapKey": {
    "key1": true,
    "key2": false
  },
  "listKey": [
    1.2,
    3.4,
    5
  ]
}"""

    @Test
    fun customJsonObjectSetGetTest2() {
        val instance = SimpleJsonObject("testObject",
            mapOf("key1" to true, "key2" to false),
            listOf(1.2, 3.4, 5.0))
        assertEquals(exceptedJson, instance.toJson(formatted = true))
        assertEquals(instance, SimpleJsonObject().load(exceptedJson))
    }
}
```

```kotlin
data class ComplexJsonObject(
    var string: String? = null,
    var map: Map<String, ComplexJsonObject>? = null,
    var list: List<ComplexJsonObject>? = null) : CSJsonObject() {

    init {
        string?.let { set("stringKey", it) }
        map?.let { set("mapKey", it) }
        list?.let { set("listKey", it) }
    }

    override fun onLoaded() {
        string = get("stringKey")
        map = getJsonObjectMap("mapKey", ComplexJsonObject::class)
        list = getJsonObjectList("listKey", ComplexJsonObject::class)
    }
}

@RunWith(RobolectricTestRunner::class)
class ComplexJsonObjectTest {
    private val expectedJson = """
{
  "stringKey": "testObject",
  "mapKey": {
    "mapKey1": {
      "stringKey": "mapTestObject1"
    },
    "mapKey2": {
      "stringKey": "mapTestObject2"
    }
  },
  "listKey": [
    {
      "stringKey": "listTestObject1"
    },
    {
      "stringKey": "listTestObject2"
    }
  ]
}""".trimStart()

    @Test
    fun customJsonObjectSetGetTest2() {
        val instance = ComplexJsonObject("testObject",
            mapOf("mapKey1" to ComplexJsonObject("mapTestObject1"),
                "mapKey2" to ComplexJsonObject("mapTestObject2")),
            listOf(ComplexJsonObject("listTestObject1"), ComplexJsonObject("listTestObject2")))

        Assert.assertEquals(expectedJson, instance.toJson(formatted = true))
        Assert.assertEquals(instance, ComplexJsonObject().load(expectedJson))
    }
}
```

```kotlin
class JsonObjectTest {
    @Test
    fun jsonObjectStoreString() {
        val json = CSJsonObject().apply { set("key", "some string") }.toJson()
        assertEquals("""{"key":"some string"}""", json)
        val value = CSJsonObject(json).getString("key")
        assertEquals(value, "some string")
    }

    @Test
    fun jsonObjectStoreBooleanAsString() {
        forceStringInJson = true
        val json = CSJsonObject().apply { set("key", false) }.toJson()
        assertEquals("""{"key":"false"}""", json)
        val value = CSJsonObject(json).getBoolean("key")
        assertEquals(value, false)
    }

    @Test
    fun jsonObjectStoreBoolean() {
        forceStringInJson = false
        val json = CSJsonObject().apply { set("key", false) }.toJson()
        assertEquals("""{"key":false}""", json)
        val value = CSJsonObject(json).getBoolean("key")
        assertEquals(value, false)
    }

    @Test
    fun jsonObjectStoreInt() {
        forceStringInJson = true
        val json = CSJsonObject().apply { set("key", int = 345) }.toJson()
        assertEquals("""{"key":"345"}""", json)
        val value = CSJsonObject(json).getInt("key")
        assertEquals(value, 345)
    }

    @Test
    fun jsonObjectStoreDouble() {
        forceStringInJson = true
        val json = CSJsonObject().apply { set("key", 213131.131331) }.toJson()
        assertEquals("""{"key":"213131.131331"}""", json)
        val value = CSJsonObject(json).getDouble("key")
        assertEquals(value, 213131.131331)
    }

    @Test
    fun jsonObjectStoreListString() {
        val value = listOf("1", "2", "3")
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":["1","2","3"]}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(value, jsonObject.getStringList("key"))
        assertEquals(listOf(1, 2, 3), jsonObject.getIntList("key"))
    }

    @Test
    fun jsonObjectStoreListFloat() {
        forceStringInJson = false
        val value: List<Float> = listOf(1f, 2.5f, 32349.89f)
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":[1,2.5,32349.89]}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(value, jsonObject.getFloatList("key"))
        assertEquals(listOf("1", "2.5", "32349.89"), jsonObject.getStringList("key"))
    }

    @Test
    fun jsonObjectStoreListDoubleAsString() {
        forceStringInJson = true
        val value: List<Double> = listOf(1.0, 2.5, 32349.89)
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":["1.0","2.5","32349.89"]}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(value, jsonObject.getDoubleList("key"))
        assertEquals(listOf("1.0", "2.5", "32349.89"), jsonObject.getStringList("key"))
    }

    @Test
    fun jsonObjectStoreListDouble() {
        forceStringInJson = false
        val value: List<Double> = listOf(1.0, 2.5, 32349.89)
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":[1,2.5,32349.89]}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(value, jsonObject.getDoubleList("key"))
        assertEquals(listOf("1", "2.5", "32349.89"), jsonObject.getStringList("key"))
    }

    @Test
    fun jsonObjectStoreMapString() {
        forceStringInJson = false
        val value: Map<String, String> =
            mapOf("key1" to "value1", "key2" to "value2", "key3" to "value3")
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":{"key1":"value1","key2":"value2","key3":"value3"}}""", json)
        val returnValue = CSJsonObject(json).getStringMap("key")
        assertEquals(value, returnValue)
    }

    @Test
    fun jsonObjectStoreIntMapToStringMap() {
        forceStringInJson = false
        val value: Map<String, Double> = mapOf("key1" to 1.2, "key2" to 2.3, "key3" to 3.4)
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":{"key1":1.2,"key2":2.3,"key3":3.4}}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(mapOf("key1" to "1.2", "key2" to "2.3", "key3" to "3.4"),
            jsonObject.getStringMap("key"))
        assertEquals(mapOf("key1" to 1.2f, "key2" to 2.3f, "key3" to 3.4f),
            jsonObject.getFloatMap("key"))
    }

    @Test
    fun jsonObjectStoreIntMapForceString() {
        forceStringInJson = false
        val value: Map<String, Double> = mapOf("key1" to 1.2, "key2" to 2.3, "key3" to 3.4)
        val json = CSJsonObject().apply { set("key", value) }.toJson()
        assertEquals("""{"key":{"key1":1.2,"key2":2.3,"key3":3.4}}""", json)
        val jsonObject = CSJsonObject(json)
        assertEquals(mapOf("key1" to "1.2", "key2" to "2.3", "key3" to "3.4"),
            jsonObject.getStringMap("key"))
        assertEquals(mapOf("key1" to 1.2f, "key2" to 2.3f, "key3" to 3.4f),
            jsonObject.getFloatMap("key"))
    }
}
```

## Renetik Android - Libraries

#### [https://github.com/renetik/renetik-android-core](https://github.com/renetik/renetik-android-core/) ➜ [Documentation](https://renetik.github.io/renetik-android-core/)

#### [https://github.com/renetik/renetik-android-json](https://github.com/renetik/renetik-android-json/) ➜ [Documentation](https://renetik.github.io/renetik-android-json/)

#### [https://github.com/renetik/renetik-android-event](https://github.com/renetik/renetik-android-event/) ➜ [Documentation](https://renetik.github.io/renetik-android-event/)

#### [https://github.com/renetik/renetik-android-store](https://github.com/renetik/renetik-android-store/) ➜ [Documentation](https://renetik.github.io/renetik-android-store/)

#### [https://github.com/renetik/renetik-android-preset](https://github.com/renetik/renetik-android-preset/) ➜ [Documentation](https://renetik.github.io/renetik-android-preset/)

#### [https://github.com/renetik/renetik-android-framework](https://github.com/renetik/renetik-android-framework/) ➜ [Documentation](https://renetik.github.io/renetik-android-framework/)
