package renetik.android.store.property.late

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.store.json.CSStoreJsonObject

@RunWith(RobolectricTestRunner::class)
class CSBooleanLateStoreEventPropertyTest {

    private val store = CSStoreJsonObject()

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