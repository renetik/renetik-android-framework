package renetik.android.store

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.getApplication
import org.robolectric.annotation.Config
import renetik.android.core.base.CSTestApplication
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.json.CSJson
import renetik.android.json.toJson
import renetik.android.store.dataProperty
import renetik.android.store.reload
import renetik.android.store.type.CSJsonObjectStore.Companion.CSJsonObjectStore

@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class CSJsonObjectStoreTest {

    val store = CSJsonObjectStore(mapOf())

    @Before
    fun prepare() {
        app = getApplication()
        CSJson.forceString = true
    }

    @Test
    fun defaultStoreEventTest() {
        var loaded = 0
        var loadedData: Map<String, Any?> = emptyMap()
        var changedData: Map<String, Any?> = emptyMap()
        var changed = 0
        store.eventLoaded.onChange { loaded += 1; loadedData = it.data }
        store.eventChanged.onChange { changed += 1; changedData = it.data }
        store.clear()
        assertEquals("loaded:0, changed:0", "loaded:$loaded, changed:$changed")

        val json = """{"string":"new value 2"}"""
        store.reload(json)
        assertEquals("loaded:1, changed:1", "loaded:$loaded, changed:$changed")
        assertEquals(json, loadedData.toJson())
        assertEquals(json, changedData.toJson())

        store.clear()
        assertEquals("loaded:2, changed:2", "loaded:$loaded, changed:$changed")
        assertEquals("""{}""", loadedData.toJson())
        assertEquals("""{}""", changedData.toJson())
    }

    @Test
    fun defaultStoreTest() {
        var string: String by store.dataProperty("string", "string")
        var bool: Boolean by store.dataProperty("bool", false)
        var int: Int by store.dataProperty("int", 5)
        assertEquals("""{}""", store.toJson())

        string = "new value"
        bool = true
        int = 10
        assertEquals(
            """{"bool":"true","int":"10","string":"new value"}""",
            store.toJson()
        )

        store.reload("""{"string":"new value 2"}""")
        assertEquals("new value 2", string)
        assertEquals(false, bool)
        assertEquals(5, int)
        CSJson.forceString = false
    }

    @After
    fun backToDefault() {
        CSJson.forceString = false
    }
}