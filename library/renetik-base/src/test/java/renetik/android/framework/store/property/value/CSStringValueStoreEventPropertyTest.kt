package renetik.android.framework.store.property.value

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import renetik.android.json.store.CSStringJsonStore

class CSStringValueStoreEventPropertyTest {
    private val store = CSStringJsonStore("{}")
    private var _value: String? = null
    private val property = CSStringValueStoreEventProperty(store, "key", "") {
        _value = it
    }

    @Test
    fun testInit() {
        assertEquals("", property.value)
        assertEquals(null, _value)
        assertTrue(store.has(property.key))
    }

    @Test
    fun testValue() {
        property.value = "test"
        assertEquals("test", property.value)
        assertEquals("test", _value)
        assertEquals("""{"key":"test"}""", store.jsonString)

        property.value = ""
        assertEquals("", property.value)
        assertEquals("", _value)
        assertEquals("""{"key":""}""", store.jsonString)
    }

    @Test
    fun testReload() {
        property.save(store, "test")
        assertEquals("", property.value)
        assertEquals("""{"key":"test"}""", store.jsonString)

        property.reload()
        assertEquals("test", property.value)
        assertEquals("test", _value)
    }
}