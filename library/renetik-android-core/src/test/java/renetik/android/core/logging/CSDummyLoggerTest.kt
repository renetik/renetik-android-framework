package renetik.android.core.logging

import org.junit.Assert
import org.junit.Test
import renetik.android.core.logging.CSLog.init
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogLevel.Debug
import renetik.android.core.logging.CSLogLevel.Info
import renetik.android.core.logging.CSLogLevel.Warn

class CSDummyLoggerTest {
    var event: CSLogLevel? = null
    var message: String? = null

    @Test
    fun warnTestNoInit() {
        logWarn { "test" }
    }

    @Test
    fun logWithListener() {
        init(logger = CSDummyLogger { event, message ->
            this.event = event
            this.message = message
        }, isTraceLineEnabled = false)
        logWarn { "test" }
        Assert.assertEquals(Warn, event)
        Assert.assertTrue(message!!.endsWith(" test"))
    }

    @Test
    fun isDebug() {
        val listener = { event: CSLogLevel, message: String? ->
            this.event = event
            this.message = message
        }
        init(logger = CSDummyLogger(level = Info, listener = listener),
            isTraceLineEnabled = false)
        logDebug { "test" }
        Assert.assertNull(event)

        init(logger = CSDummyLogger(level = Debug, listener = listener),
            isTraceLineEnabled = false)
        logDebug { "test2" }
        Assert.assertEquals(Debug, event)
        Assert.assertTrue(message!!.endsWith(" test2"))
    }
}
