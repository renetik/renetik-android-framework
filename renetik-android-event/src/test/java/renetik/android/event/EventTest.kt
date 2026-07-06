package renetik.android.event


import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus

/**
 * Simple event use cases
 */
@RunWith(RobolectricTestRunner::class)
class EventTest {
    @Test
    fun testListen() {
        val testEvent = event()
        var count = 0
        testEvent.listen { count += 1 }
        testEvent()
        testEvent()
        assertEquals(2, count)
    }

    @Test
    fun testArgListen() {
        val event = event<Int>()
        var count = 0
        event.listen { count += it }
        event.fire(2)
        event.fire(3)
        assertEquals(5, count)
    }

    @Test
    fun testListenOnce() {
        val testEvent = event()
        var count = 0
        testEvent.listenOnce { count += 1 }
        testEvent()
        testEvent()
        assertEquals(1, count)
    }

    @Test
    fun testArgListenOnce() {
        val event = event()
        var count = 0
        event.listenOnce { count += 1 }
        event.fire()
        event.fire()
        assertEquals(1, count)
    }

    @Test
    fun testEventCancel() {
        val event = event()
        var count = 0
        event.listen { registration, _ ->
            count += 1
            if (count == 2) registration.cancel()
        }
        event.fire()
        event.fire()
        event.fire()
        assertEquals(2, count)
    }

    @Test
    fun testStringEventCancel() {
        val event = event<String>()
        var value: String? = null
        event.listen { registration, newValue ->
            if (newValue == "second") registration.cancel()
            value = newValue
        }
        event.fire("first")
        assertEquals("first", value)
        event.fire("second")
        assertEquals("second", value)
        event.fire("third")
        assertEquals("second", value)
    }

    @Test
    fun testEventPause() {
        val event = event()
        var count = 0
        val registration = event.listen { count += 1 }
        registration.paused { event.fire() }
        assertEquals(0, count)
        event.fire()
        assertEquals(1, count)
    }

    @Test
    fun testCancelWhilePaused() {
        val event = event()
        var count = 0
        val registration = event.listen { count += 1 }
        registration.paused {
            event.fire()
            registration.cancel()
        }
        event.fire()
        assertEquals(0, count)
        assertFalse(registration.isActive)
    }

    @Test
    fun testDestructWhileRunning() {
        val model = CSModel()
        val event = event()
        var count = 0
        model + event.listen {
            count += 1
        }
        model + event.listen {
            model.destruct()
        }
        model + event.listen {
            count += 1
        }
        event.fire()
        assertEquals(1, count)
        assertTrue(model.isDestructed)
    }


    @Test
    fun testPauseResume() {
        val testEvent = event()
        var count = 0
        val registration = testEvent.listen { count += 1 }
        testEvent()
        assertEquals(1, count)
        testEvent.pause()
        registration.pause()
        registration.resume()
        testEvent()
        assertEquals(1, count)
        testEvent.resume()
        testEvent()
        assertEquals(2, count)
    }
}