package renetik.android.java.event

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.CSApplicationMock

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", application = CSApplicationMock::class)
class CSEventTest {

    var eventOneCounter = 0
    var eventOneLastValue = ""
    private val eventOne = event<String>()

    @Test
    fun applicationNameTest() {
        eventOne.run { registration, value ->
            eventOneCounter++
            eventOneLastValue = value
            registration.cancel()
        }
        eventOne.fire("testOne")
        eventOne.fire("testTwo")
        assertEquals(1, eventOneCounter)
        assertEquals("testOne", eventOneLastValue)
    }
}


