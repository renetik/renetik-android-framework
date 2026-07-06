package renetik.android.event


import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.logging.CSDummyLogger
import renetik.android.core.logging.CSLog.init
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.registration.plus
import renetik.android.testing.CSAssert.assert

@RunWith(RobolectricTestRunner::class)
class EventOwnerEventTest {

    @Before
    fun before() = init(logger = CSDummyLogger(), isTraceLineEnabled = false)

    @Test
    fun testUnregisteredAfterNilled() {
        val owner = CSModel()
        val testEvent = event()
        var count = 0
        owner + testEvent.listen { count += 1 }
        testEvent()
        testEvent()
        assertEquals(2, count)
        owner.destruct()
        testEvent()
        assertEquals(2, count)
    }

    @Test
    fun testOwnerDestroyed() {
        val owner = CSModel().apply { destruct() }
        assert(expected = true, owner.registrations.isCanceled)
        val testEvent = event()
        var count = 0
        val registration = owner + testEvent.listen { count += 1 }
        assert(expected = true, registration.isCanceled)
        testEvent()
        testEvent()
        assert(expected = 0, count)
    }
}
