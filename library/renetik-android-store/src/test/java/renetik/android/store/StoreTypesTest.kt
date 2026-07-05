package renetik.android.store

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.android.content.getString
import renetik.android.core.android.content.temporaryFile
import renetik.android.core.base.CSTestApplication
import renetik.android.json.CSJson
import renetik.android.json.toJson
import renetik.android.store.load
import renetik.android.store.property
import renetik.android.store.type.CSFileJsonStore.Companion.CSFileJsonStore
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.store.type.CSPreferencesJsonStore
import renetik.android.store.type.CSPreferencesStore
import renetik.android.store.type.CSStringJsonStore
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class StoreTypesTest {

    @Before
    fun prepare() {
        CSJson.isJsonPretty = false
    }

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
        assertEquals(
            """{"key2":123,"key3":{"lateStringId":"new value"}}""",
            store.preferences.getString("property")
        )

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
        assertEquals(
            """{"property":{"key1":"new value","key2":123}}""",
            store.preferences.getString(store.key)
        )

        val store2: CSStore = CSPreferencesJsonStore(context)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(123, property2.int)
        assertNull(property2.jsonObject.nullString)
    }

    @Test
    fun testFileJsonStoreTracksInMemoryState() {
        val store = CSFileJsonStore(context.temporaryFile("json"), isJsonPretty = false)
        val property: StoreTypesTestData by store.property("property")
        assertEquals(5, property.int)

        property.string = "new value"
        property.int = 123
        property.jsonObject.lateString = "new value"

        val expected =
            """{"property":{"key1":"new value","key2":123,"key3":{"lateStringId":"new value"}}}"""
        // File persistence is asynchronous in the current CSFileJsonStore design.
        // This test documents the current contract and does not treat close() as a flush barrier.
        assertEquals(expected, store.toJson())
        store.close()
    }

    @Test
    fun testFileJsonStoreLoadsExistingFile() {
        val file = context.temporaryFile("json")
        file.writeText(
            """{"property":{"key1":"new value","key2":123,"key3":{"lateStringId":"new value"}}}"""
        )

        val store2: CSStore = CSFileJsonStore(file, isJsonPretty = false)
        val property2: StoreTypesTestData by store2.property("property")
        assertEquals("new value", property2.string)
        assertEquals(123, property2.int)
        assertEquals("new value", property2.jsonObject.lateString)
    }
}
