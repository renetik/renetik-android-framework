package renetik.android.core

import org.junit.Test
import renetik.android.core.logging.CSLog

class CSLogTest {
    @Test
    fun warnTestNoInit() {
        CSLog.logWarn("test")
    }
}