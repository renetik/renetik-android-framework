package renetik.android.framework.store.property.late

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import renetik.android.framework.store.CSMapStore

class CSBooleanLateStoreEventPropertyTest {

    private val store = CSMapStore()

    @Test
    fun test() {
        var _value = true
        val property = CSBooleanLateStoreEventProperty(store, "key") {
            _value = it
        }
        assertTrue(_value)
        property.value = false
        assertFalse(_value)
        assertFalse(property.value)
    }
}