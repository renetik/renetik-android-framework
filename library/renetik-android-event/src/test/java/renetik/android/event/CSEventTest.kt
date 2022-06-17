package renetik.android.event

import org.junit.Assert.assertEquals
import org.junit.Test
import renetik.android.event.CSEvent.Companion.event

class CSEventTest {

    private var eventOneCounter = 0
    private var eventOneValue = ""
    private val eventOne = event<String>()

    @Test
    fun fireTwiceAndCancel() {
        eventOne.add { registration, value ->
            eventOneCounter++
            eventOneValue = value
            if (eventOneCounter == 2) registration.cancel()
        }
        eventOne.fire("testOne")
        eventOne.fire("testTwo")
        eventOne.fire("testThree")
        assertEquals(2, eventOneCounter)
        assertEquals("testTwo", eventOneValue)
    }

    @Test
    fun twoListenersCancelBothInSecond() {
        val eventOneRegistration = eventOne.add { _, value ->
            eventOneCounter++
            eventOneValue = value
        }
        eventOne.add { registration, value ->
            eventOneCounter++
            eventOneValue = value
            registration.cancel()
            eventOneRegistration.cancel()
        }
        eventOne.fire("testOne")
        assertEquals(2, eventOneCounter)
        assertEquals("testOne", eventOneValue)

        eventOne.fire("testTwo")
        assertEquals(2, eventOneCounter)
        assertEquals("testOne", eventOneValue)
    }

    @Test
    fun twoListenersAddSecondWhileRunning() {
        eventOne.add { eventOneRegistration, value1 ->
            eventOneCounter++
            eventOneValue = value1
            if (eventOneCounter == 1)
                eventOne.add { registration, value2 ->
                    eventOneCounter++
                    eventOneValue = value2
                    registration.cancel()
                    eventOneRegistration.cancel()
                }
        }

        eventOne.fire("testOne")
        assertEquals(1, eventOneCounter)
        assertEquals("testOne", eventOneValue)

        eventOne.fire("testTwo")
        assertEquals(3, eventOneCounter)
        assertEquals("testTwo", eventOneValue)

        eventOne.fire("testThree")
        assertEquals(3, eventOneCounter)
        assertEquals("testTwo", eventOneValue)
    }
}


