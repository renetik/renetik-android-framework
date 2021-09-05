package renetik.android.framework.store

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CSCSStoreInterfaceTest {

    private val store = CSMapStore()

    @Before
    fun before() = store.clear()

    @Test
    fun testStringProperty() {
        val property = store.property("property", value = "value")
        assertEquals("value", property.value)
        property.value = "value2"
        assertEquals("value2", property.value)
    }

    @Test
    fun testStoredStringProperty() {
        val property = store.lateStringProperty("property")
        property.value = "value"
        assertEquals("value", property.value)
    }

    @Test
    fun testIntProperty() {
        val property = store.property("property", value = 2)
        assertEquals(2, property.value)
        property.value = 3
        assertEquals(3, property.value)
    }

    @Test
    fun testNullableIntProperty() {
        val property = store.nullableProperty("property", null)
        assertEquals(null, property.value)
        property.value = 5
        assertEquals(5, property.value)
    }


}