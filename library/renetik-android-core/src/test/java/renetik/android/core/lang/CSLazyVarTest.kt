package renetik.android.core.lang

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import renetik.android.core.kotlin.isInitialized
import renetik.android.core.lang.lazy.CSLazyVar
import renetik.android.core.lang.lazy.CSLazyVar.Companion.lazyVar

class CSLazyVarTest {
    @Test
    fun testLazyVar() {
        val lazyVar: CSLazyVar<String> = lazyVar { "initial" }
        var testVar: String by lazyVar
        assertFalse(lazyVar.isInitialized)

        assertEquals("initial", testVar)
        assertTrue(lazyVar.isInitialized())

        testVar = "test"
        assertEquals("test", testVar)
    }
}