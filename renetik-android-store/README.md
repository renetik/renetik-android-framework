[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

# Renetik Android Store

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Renetik Android utilities to improve and speed up application development while writing readable code.
Used as library in many projects and improving it while developing new projects.
I am open for [Hire](https://renetik.github.io) or investment in my mobile app music production & perfromance project Renetik Instruments www.renetik.com.

## Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-store:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest Renetik Android `master`.

```kotlin
class StoreTypesTestData : CSJsonObjectStore() {
    var string: String by property("key1", default = "initial")
    var int: Int by property("key2", default = 5)
    val jsonObject: TestStringData by property("key3")
}

class StoreTypesTest {
    @Test
    fun testJsonObjectStore() {
        val store = CSJsonObjectStore()
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.jsonObject.lateString = "new value"
        assertEquals("""{"property":{"key3":{"lateStringId":"new value"}}}""", store.toJson())
        property.string = "new value"
        property.int = 123

        val store2 = CSJsonObjectStore().load(store)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(123, property2.int)
        assertEquals("new value", property2.jsonObject.lateString)
    }

    @Test
    fun testStringJsonStore() {
        val store = CSStringJsonStore()
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.string = "new value"
        assertEquals("""{"property":{"key1":"new value"}}""", store.jsonString)
        property.jsonObject.lateString = "new value"
        property.int = 100

        val store2 = CSStringJsonStore(store.jsonString)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(100, property2.int)
        assertEquals("new value", property2.jsonObject.lateString)
    }

    @Test
    fun testPreferencesStore() {
        val store = CSPreferencesStore(context)
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.int = 123
        property.jsonObject.lateString = "new value"
        assertEquals("""{"key2":123,"key3":{"lateStringId":"new value"}}""",
            store.preferences.getString("property"))

        val store2: CSStore = CSPreferencesStore(context)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("initial", property2.string)
        assertEquals(123, property2.int)
        assertEquals("new value", property2.jsonObject.lateString)
    }

    @Test
    fun testPreferencesJsonStore() {
        val store = CSPreferencesJsonStore(context)
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.string = "new value"
        property.int = 123
        assertEquals("""{"property":{"key1":"new value","key2":123}}""",
            store.preferences.getString(store.key))

        val store2: CSStore = CSPreferencesJsonStore(context)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(123, property2.int)
        assertNull(property2.jsonObject.nullString)
    }

    @Test
    fun testFileJsonStore() {
        val store = CSFileJsonStore(context, "file")
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.string = "new value"
        property.int = 123
        property.jsonObject.lateString = "new value"
        assertEquals(
            """{"property":{"key1":"new value","key2":123,"key3":{"lateStringId":"new value"}}}""",
            store.file.readString())

        val store2: CSStore = CSFileJsonStore(context, "file")
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(123, property2.int)
        assertEquals("new value", property2.jsonObject.lateString)
    }
}
```

```kotlin
class ValueStorePropertyTest {
    private val store: CSStore = CSJsonObjectStore()

    @Test
    fun testStringProperty() {
        var eventCount = 0
        var value: String by store.property("key", default = "initial") { eventCount += 1 }
        assertEquals("initial", value)
        value = "new value"
        assertEquals("""{"key":"new value"}""", store.toJson())

        store.reload("""{"key":"new value 2"}""")
        val value2: String by store.property("key", default = "")

        assertEquals("new value 2", value)
        assertEquals("new value 2", value2)
        assertEquals(2, eventCount)
    }

    @Test
    fun testBooleanProperty() {
        var value: Boolean by store.property("key", default = false)
        assertEquals(false, value)
        value = true
        assertEquals("""{"key":true}""", store.toJson())

        store.reload(store.toJson())
        val value2: Boolean by store.property("key", default = false)
        assertEquals(true, value2)
    }

    @Test
    fun testIntProperty() {
        var value: Int by store.property("key", default = 5)
        assertEquals(5, value)
        value = 345
        assertEquals("""{"key":345}""", store.toJson())

        store.reload(store.toJson())
        val value2: Int by store.property("key", default = 10)
        assertEquals(345, value2)
    }

    @Test
    fun testFloatProperty() {
        var eventCount = 0
        var value: Float by store.property("key", default = 1.5f) { eventCount += 1 }
        assertEquals(1.5f, value)
        value = 2.5f
        assertEquals("""{"key":2.5}""", store.toJson())

        store.reload("""{"key":2.3}""")
        assertEquals(2, eventCount)

        val value2: Float by store.property("key", default = 542f)
        assertEquals(2.3f, value2)
    }

    @Test
    fun testDoubleProperty() {
        var eventCount = 0
        var value: Double by store.property("key", default = 1.5) { eventCount += 1 }
        assertEquals(1.5, value, 0.0)
        value = 2.3
        assertEquals("""{"key":"2.3"}""", store.toJson(forceString = true))

        store.reload(store.toJson())
        val value2: Double by store.property("key", default = 5.5)
        assertEquals(2.3, value2, 0.0)
        assertEquals(1, eventCount)
    }

    @Test
    fun testListItemValueProperty() {
        var value: TestIdItem by store.property("key",
            TestIdItems, defaultIndex = 1)
        assertEquals(Second, value)
        value = Third
        assertEquals("""{"key":"id3"}""", store.toJson())

        store.reload(store.toJson())
        val value2: TestIdItem by store.property("key",
            TestIdItems, default = First)
        assertEquals(Third, value2)
    }

    @Test
    fun testListValueProperty() {
        var value: List<TestIdItem> by store.property("key",
            TestIdItems, default = listOf(First))
        assertEquals(null, store.get("key"))
        value = listOf(First, Third)
        assertEquals("""{"key":"id1,id3"}""", store.toJson())

        store.reload(store.toJson())
        val value2: List<TestIdItem> by store.property("key",
            TestIdItems, default = listOf(Second))
        assertEquals(listOf(First, Third), value2)
    }

    @Test
    fun testJsonValueProperty() {
        val value: TestStringData by store.property("key")
        assertEquals("""{}""", store.toJson())
        assertEquals("string", value.string)
        assertNull(value.nullString)
        assertThrows { value.lateString }

        val newString = "new string"
        value.string = newString
        assertEquals("""{"key":{"stringId":"new string"}}""", store.toJson())
        value.nullString = newString
        value.lateString = newString

        store.reload(store.toJson())
        val value2: TestStringData by store.property("key")
        assertEquals(newString, value2.string)
        assertEquals(newString, value2.nullString)
        assertEquals(newString, value2.lateString)
    }

    @Test
    fun testJsonValuePropertyDefault() {
        val value: TestStringData by store.property("key",
            TestStringData(string = "string 2"))
        assertEquals("""{}""", store.toJson())
        assertEquals("string 2", value.string)
        assertEquals(null, value.nullString)
        assertThrows { value.lateString }

        val newString = "string 3"
        value.string = newString
        assertEquals("""{"key":{"stringId":"string 3"}}""", store.toJson())
        value.nullString = newString
        value.lateString = newString

        store.reload(store.toJson())
        val value2: TestStringData by store.property("key")
        assertEquals(newString, value2.string)
        assertEquals(newString, value2.nullString)
        assertEquals(newString, value2.lateString)
    }

    @Test
    fun testJsonListValueProperty() {
        val property = store.property<TestStringData>("key", mutableListOf())
        assertEquals("""{}""", store.toJson())
        property.value.add(TestStringData())
        property.value.add(TestStringData())
        property.value.add(TestStringData(lateString = "string"))
        property.save()
        assertEquals("""{"key":[{},{},{"lateStringId":"string"}]}""", store.toJson())
        property.value.last().lateString = "new string"
        property.save()

        store.reload(store.toJson())
        val value: List<TestStringData> by store.property("key", listOf())
        assertEquals("new string", value.last().lateString)
    }
}
```
```kotlin
class NullStorePropertyTest {
    private val store: CSStore = CSJsonObjectStore()

    @Test
    fun testStringProperty() {
        var value: String? by store.nullStringProperty("key", "initial")
        value = "new value"
        assertEquals("""{"key":"new value"}""", store.toJson())
        value = null
        assertEquals("""{}""", store.toJson())
        assertEquals("initial", value)

        store.reload(store.toJson())
        val value2: String? by store.nullStringProperty("key")
        assertNull(value2)
    }

    @Test
    fun testBooleanProperty() {
        CSJson.forceStringInJson = true
        var value: Boolean? by store.nullBoolProperty("key")
        assertNull(value)
        value = true
        assertEquals("""{"key":"true"}""", store.toJson())

        store.reload(store.toJson())
        val value2: Boolean? by store.nullBoolProperty("key")
        assertEquals(true, value2)
        CSJson.forceStringInJson = false
    }

    @Test
    fun testIntProperty() {
        var value: Int? by store.nullIntProperty("key", 5)
        value = 10
        assertEquals("""{"key":10}""", store.toJson())
        value = null
        assertEquals("""{}""", store.toJson())
        assertEquals(5, value)

        store.reload(store.toJson())
        val value2: Int? by store.nullIntProperty("key")
        assertNull(value2)
    }

    @Test
    fun testFloatProperty() {
        var value: Float? by store.nullFloatProperty("key", 1.5f)
        value = 2.5f
        assertEquals("""{"key":2.5}""", store.toJson())
        value = null
        assertEquals("""{}""", store.toJson())
        assertEquals(1.5f, value)

        store.reload(store.toJson())
        val value2: Float? by store.nullFloatProperty("key")
        assertNull(value2)
    }

    @Test
    fun testListItemValueProperty() {
        var value: TestIdItem? by store.nullListItemProperty("key",
            TestIdItems, defaultIndex = 1)
        value = First
        assertEquals("""{"key":"id1"}""", store.toJson())
        value = null
        assertEquals("""{}""", store.toJson())
        assertEquals(Second, value)

        store.reload(store.toJson())
        val value2: TestIdItem? by store.nullListItemProperty("key", TestIdItems)
        assertNull(value2)
    }

    @Test
    fun testJsonProperty() {
        val default = TestStringData(string = "string 2")
        var value: TestStringData? by store.nullJsonProperty("key", default)
        assertEquals("""{}""", store.toJson())
        assertEquals("string 2", value!!.string)
        assertEquals(null, value!!.nullString)
        assertThrows { value!!.lateString }

        val newString = "string 3"
        value!!.string = newString
        assertEquals("""{"key":{"stringId":"string 3"}}""", store.toJson())
        value!!.nullString = newString
        value!!.lateString = newString

        value = null
        assertEquals("""{}""", store.toJson())
        assertEquals(default, value)
        assertNotSame(default, value)

        store.reload(store.toJson())
        val value2: TestStringData? by store.nullJsonProperty("key")
        assertNull(value2)
    }
}
```
```kotlin
class LateStorePropertyTest {
    private val store: CSStore = CSJsonObjectStore()

    @Test
    fun testStringProperty() {
        var eventCount = 0
        var newValue: String? = null
        var value: String by store.lateStringProperty("key") {
            newValue = it
            eventCount += 1
        }
        value = "value 1"

        assertEquals("""{"key":"value 1"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"value 2"}""")
        assertEquals(newValue, "value 2")
        assertEquals(2, eventCount)
    }


    @Test
    fun testBooleanProperty() {
        var newValue: Boolean? = null
        var value: Boolean by store.lateBoolProperty("key") { newValue = it }
        value = false

        assertEquals("""{"key":false}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":true}""")
        assertTrue(newValue!!)
    }

    @Test
    fun testIntProperty() {
        forceStringInJson = true
        var newValue: Int? = null
        var value: Int by store.lateIntProperty("key") { newValue = it }
        value = 34

        assertEquals("""{"key":"34"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"56"}""")
        assertEquals(56, newValue)
        forceStringInJson = false
    }

    @Test
    fun testListItemValueProperty() {
        var eventCount = 0
        var newValue: TestIdItem? = null
        var value: TestIdItem by store.lateListItemProperty("key", TestIdItems) {
            newValue = it
            eventCount += 1
        }
        value = Fourth

        assertEquals("""{"key":"id4"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"id2"}""")
        assertEquals(newValue, Second)
        assertEquals(2, eventCount)
    }

    @Test
    fun testJsonProperty() {
        var newValue: TestStringData? = null
        var value: TestStringData by store.lateJsonProperty("key") { newValue = it }
        value = TestStringData()

        assertEquals("""{"key":{}}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":{"stringId":"string 1"}}""")
        assertEquals(newValue, TestStringData().apply { string = "string 1" })
    }

    @Test
    fun testLateJsonListProperty() {
        var newValue: List<TestStringData>? = null
        var value: List<TestStringData> by store.lateJsonListProperty("key") { newValue = it }
        value = listOf(TestStringData(), TestStringData(lateString = "string 1"))

        assertEquals("""{"key":[{},{"lateStringId":"string 1"}]}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":[{"nullStringId":"string 2"},{}]}""")
        assertEquals(newValue,
            listOf(TestStringData(nullString = "string 2"), TestStringData()))
    }

    @Test
    fun testLateJsonListListProperty() {
        var newValue: List<List<TestStringData>>? = null
        var value: List<List<TestStringData>> by store.lateJsonListListProperty("key") {
            newValue = it
        }
        assertThrows { value.last() }
        value = listOf(listOf(TestStringData()), listOf(), listOf(TestStringData("test")))

        assertEquals("""{"key":[[{}],[],[{"stringId":"test"}]]}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":[[{"stringId":"test 2"}],[]]}""")
        assertEquals("test 2", value.first().first().string)
    }
}
```

## Renetik Android Libraries

See [Renetik Android](https://github.com/renetik/renetik-android/) for all modules and release coordinates.
