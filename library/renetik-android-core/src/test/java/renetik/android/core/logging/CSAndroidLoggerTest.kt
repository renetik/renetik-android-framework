package renetik.android.core.logging

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.logging.CSLog.init
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogLevel.Debug
import renetik.android.core.logging.CSLogLevel.Info
import renetik.android.core.logging.CSLogLevel.Warn

@RunWith(RobolectricTestRunner::class)
class CSAndroidLoggerTest {

    private var event: CSLogLevel? = null
    private var message: String? = null
    private val listener = { event: CSLogLevel, message: String?, _: Throwable? ->
        this.event = event
        this.message = message
    }

    private fun traceFragment(method: String) =
        "${CSAndroidLoggerTest::class.java.name}${'$'}$method(CSAndroidLoggerTest.kt:"

    @Test
    fun logWithListener() {
        init(logger = CSAndroidLogger(
            tag = "TestLog", level = Debug, listener = listener),
            isTraceLineEnabled = true)
        logWarn { "test" }
        assertEquals(Warn, event)
        assertNotNull(message)
        assertTrue(message!!.contains(traceFragment("logWithListener")))
        assertTrue(message!!.endsWith(" test"))
    }

    @Test
    fun logEmpty() {
        init(logger = CSAndroidLogger(
            tag = "TestLog", level = Debug, listener = listener),
            isTraceLineEnabled = true)
        logInfo()
        assertEquals(Info, event)
        assertNotNull(message)
        assertTrue(message!!.contains(traceFragment("logEmpty")))
        assertFalse(message!!.endsWith(" "))
    }

    @Test
    fun isDebug() {
        init(logger = CSAndroidLogger(
            tag = "TestLog", level = Info, listener = listener),
            isTraceLineEnabled = true)
        logDebug { "test" }
        assertNull(event)
        assertNull(message)

        init(logger = CSAndroidLogger(
            tag = "TestLog", level = Debug, listener = listener),
            isTraceLineEnabled = true)
        logDebug { "test2" }
        assertEquals(Debug, event)
        assertNotNull(message)
        assertTrue(message!!.contains(traceFragment("isDebug")))
        assertTrue(message!!.endsWith(" test2"))
    }
}
