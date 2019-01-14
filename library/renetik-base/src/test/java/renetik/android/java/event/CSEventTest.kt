package renetik.android.java.event

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.CSApplicationMock

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", application = CSApplicationMock::class)
class CSEventTest {

    private var eventOneCounter = 0
    private var eventOneValue = ""
    private val eventOne = event<String>()

    @Test
    fun fireTwiceAndCancel() {
        eventOne.run { registration, value ->
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
        val eventOneRegistration = eventOne.run { _, value ->
            eventOneCounter++
            eventOneValue = value
        }
        eventOne.run { registration, value ->
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
        eventOne.run { eventOneRegistration, value1 ->
            eventOneCounter++
            eventOneValue = value1
            if (eventOneCounter == 1)
                eventOne.run { registration, value2 ->
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


