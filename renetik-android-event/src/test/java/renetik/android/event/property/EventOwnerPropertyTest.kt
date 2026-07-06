package renetik.android.event.property


import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.plus

/**
 * Event property unregister after owner nulled
 */
@RunWith(RobolectricTestRunner::class)
class EventOwnerPropertyTest {
    class SomeClass(parent: SomeClass? = null) : CSModel(parent) {
        val string = property("initial value")

        init {
            this + parent?.string?.onChange { string.value = it }
        }
    }

    @Test
    fun testUnregisteredAfterNilled() {
        val instance1 = SomeClass()
        val instance2 = SomeClass(instance1)
        val instance3 = SomeClass(instance2)
        assertEquals("initial value", instance3.string.value)
        instance1.string.value = "first value"
        assertEquals("first value", instance3.string.value)
        instance2.destruct()
        instance1.string.value = "second value"
        assertEquals("first value", instance3.string.value)
    }
}