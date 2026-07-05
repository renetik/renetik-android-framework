package renetik.android.store

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.getApplication
import org.robolectric.annotation.Config
import renetik.android.core.base.CSTestApplication
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.store.type.CSPreferencesStore

@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class SharedPreferencesStoreTest {

    @Before
    fun prepare() {
        app = getApplication()
    }

    private fun store(id: String = "test") = CSPreferencesStore(app, id)

    @Test
    fun stringRoundTripSurvivesReload() {
        store().set("key", "value")
        // a new store instance over the same preferences sees the value
        assertEquals("value", store().getString("key"))
    }

    @Test
    fun clearRemovesKey() {
        val store = store()
        store.set("key", "value")
        assertTrue(store.has("key"))
        store.clear("key")
        assertFalse(store.has("key"))
        assertNull(store.getString("key"))
    }

    @Test
    fun settingNullClearsKey() {
        val store = store()
        store.set("key", "value")
        store.set("key", null as String?)
        assertFalse(store.has("key"))
    }

    @Test
    fun listAndMapRoundTrip() {
        val store = store()
        store.set("list", listOf("a", "b"))
        store.set("map", mapOf("x" to "1"))
        assertEquals(listOf("a", "b"), store().getList("list"))
        assertEquals("1", store().getMap("map")!!["x"])
    }

    @Test
    fun eventChangedFiresOnSet() {
        val store = store()
        var changed = 0
        store.eventChanged.onChange { changed += 1 }
        store.set("key", "value")
        assertEquals(1, changed)
    }

    @Test
    fun propertySurvivesStoreReload() {
        var volume: Int by store("props").property("volume", default = 5)
        volume = 42
        val reloaded: Int by store("props").property("volume", default = 5)
        assertEquals(42, reloaded)
    }
}
