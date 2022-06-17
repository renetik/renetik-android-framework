package renetik.android.framework.store.property.nullable

import org.junit.Assert.*
import org.junit.Test
import renetik.android.framework.store.json.CSStringJsonStore

class CSBooleanNullableStoreEventPropertyTest {
    private val store = CSStringJsonStore("{}")
    private var _value: Boolean? = null
    private val property = CSBooleanNullableStoreEventProperty(store, "key", true) {
        _value = it
    }

    @Test
    fun testInit() {
        assertEquals(true, property.value)
        assertEquals(null, _value)
    }

    @Test
    fun testValue() {
        property.value = false
        assertEquals(false, property.value)
        assertEquals(false, _value)
        assertTrue(store.has(property.key))
        assertEquals("""{"key":"false"}""", store.jsonString)

        property.value = null
        assertEquals(null, property.value)
        assertEquals(null, _value)
        assertFalse(store.has(property.key))
        assertEquals("{}", store.jsonString)
    }

//    @Test
//    fun testReload() {
//        assertEquals(true, property.value)
//        property.save(store, false)
//        property.reload()
//        assertEquals(false, property.value)
//
//        assertEquals("""{"key":"false"}""", store.jsonString)
//        assertEquals(false, property.value)
//        assertEquals(false, _value)
//    }

}