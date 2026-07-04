package renetik.android.event.property


import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.change.onChange
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.paused

/**
 * Simple event property use cases
 */
@RunWith(RobolectricTestRunner::class)
class EventPropertyTest {

    @Test
    fun testOnChange() {
        var changeCount = 0
        var value: String by property("initial") { changeCount += 1 }
        value = "second"
        value = "third"
        assertEquals(changeCount, 2)
        assertEquals("third", value)
    }

    @Test
    fun testOnApply() {
        var changeCount = 0
        var value: String by property("initial") { changeCount += 1 }.fire()
        value = "second"
        value = "third"
        assertEquals(changeCount, 3)
        assertEquals("third", value)
    }

    @Test
    fun testNullable() {
        var changeCount = 0
        var value: Int? by property { changeCount += 1 }
        value = 0
        value = value!! + 2
        value = value!! + 3
        assertEquals(5, value)
        assertEquals(3, changeCount)
    }

    @Test
    fun testEquals() {
        var changeCount = 0
        var value: String by property("") { changeCount += 1 }
        value = "second"
        value = "second"
        assertEquals(changeCount, 1)
        assertEquals("second", value)
    }

//    @Test
//    fun testNotFireAndOnChangeOnce() {
//        var changeCount = 0
//        val property: CSProperty<String> = property("")
//        property.onChangeOnce { changeCount += 1 }
//        property.value("one", fire = false)
//        property.value = "two"
//        property.value = "three"
//        assertEquals(changeCount, 1)
//        assertEquals("three", property.value)
//    }

    @Test
    fun testEventCancel() {
        var changeCount = 0
        val property: CSProperty<Int> = property(0)
        property.onChange { registration, value ->
            changeCount += value
            if (changeCount > 2) registration.cancel()
        }
        property.value = 1
        property.value = 2
        property.value = 3
        assertEquals(changeCount, 3)
    }

    @Test
    fun testPropertyRegistrationPause() {
        var changeCount = 0
        val property: CSProperty<Int> = property(0)
        val registration = property.onChange { changeCount += it }
        registration.paused { property.value = 1 }
        assertEquals(changeCount, 0)
        property.value = 2
        assertEquals(changeCount, 2)
    }

    @Test
    fun testPropertyPause() {
        var changeCount = 0
        val property: CSProperty<Int> = property(0)
        property.onChange { changeCount += 1 }
        property.paused {
            property.value = 2
            assertEquals(0, changeCount)
            assertEquals(2, property.value)
        }
        assertEquals(1, changeCount)
        assertEquals(2, property.value)
    }
}