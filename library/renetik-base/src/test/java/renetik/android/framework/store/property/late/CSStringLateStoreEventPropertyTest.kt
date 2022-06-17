package renetik.android.framework.store.property.late

import org.junit.Assert.assertEquals
import org.junit.Test
import renetik.android.framework.store.json.CSStoreJsonObject

class CSStringLateStoreEventPropertyTest {
    private val store = CSStoreJsonObject()

    @Test
    fun test() {
        var _value = "none"
        val property = CSStringLateStoreEventProperty(store, "key") { _value = it }
        property.value = "value"
        assertEquals("value", _value)
        assertEquals("value", property.value)
    }
}