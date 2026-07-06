package renetik.android.event.registration


import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.base.CSTestApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = CSTestApplication::class)
class FunctionRegistrationTest {
    @Test
    fun testCancelWhilePaused() {
        var count = 0
        val registration = CSFunctionRegistration { count += 1 }
        registration.paused {
            registration.invoke()
            registration.cancel()
        }
        registration.invoke()
        Assert.assertEquals(0, count)
        Assert.assertFalse(registration.isActive)
    }
}
