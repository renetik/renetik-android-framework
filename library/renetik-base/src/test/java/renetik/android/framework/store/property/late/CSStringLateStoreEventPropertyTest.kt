package renetik.android.framework.store.property.late

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import renetik.android.framework.store.CSMapStore

class CSStringLateStoreEventPropertyTest {
    private val store = CSMapStore()

    @Test
    fun test() {
        var _value = "none"
        val property = CSStringLateStoreEventProperty(store, "key") { _value = it }
        property.value = "value"
        assertEquals("value", _value)
        assertEquals("value", property.value)
    }
}