package renetik.android.event.property


import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import renetik.android.event.property.CSProperty.Companion.lateProperty

/**
 * Simple event property use cases
 */
class LateEventPropertyTest {

    @Test
    fun testOnChange() {
        var count = 0
        var value: String by lateProperty { count += 1 }
        assertThrows(Exception::class.java) { print(value) }
        value = "first"
        value = "second"
        assertEquals(count, 2)
        assertEquals("second", value)
    }

    @Test
    fun testOnApply() {
        var count = 0
        val property = lateProperty<String> { count += 1 }
        property.value = "first"
        assertEquals(count, 1)
        property.fire()
        assertEquals(count, 2)
        property.value = "second"
        assertEquals("second", property.value)
    }
}