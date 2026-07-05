package renetik.android.store.property

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.toJson
import renetik.android.store.CSStore
import renetik.android.store.nullDoubleProperty
import renetik.android.store.property
import renetik.android.store.reload
import renetik.android.store.type.CSJsonObjectStore

@RunWith(RobolectricTestRunner::class)
class DoubleLongStorePropertyTest {
    private val store: CSStore = CSJsonObjectStore()

    @Test
    fun doublePropertyRoundTrip() {
        var value: Double by store.property("key", default = 0.5)
        assertEquals(0.5, value, 0.0)
        value = 2.25
        store.reload(store.toJson())
        val value2: Double by store.property("key", default = 0.0)
        assertEquals(2.25, value2, 0.0)
    }

    @Test
    fun longPropertyRoundTrip() {
        var value: Long by store.property("key", default = 0L)
        value = 9007199254740991L
        store.reload(store.toJson())
        val value2: Long by store.property("key", default = 0L)
        assertEquals(9007199254740991L, value2)
    }

    @Test
    fun nullDoublePropertyDefaultsAndClears() {
        var value: Double? by store.nullDoubleProperty("key")
        assertNull(value)
        value = 1.75
        assertEquals(1.75, value!!, 0.0)
        value = null
        assertNull(value)
    }
}
