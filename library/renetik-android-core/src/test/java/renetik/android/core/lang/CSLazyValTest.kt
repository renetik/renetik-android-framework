package renetik.android.core.lang

import org.junit.Assert
import org.junit.Test
import renetik.android.core.kotlin.isInitialized
import renetik.android.core.lang.lazy.CSLazyVal
import renetik.android.core.lang.lazy.CSLazyVal.Companion.lazyVal


class CSLazyValTest {
    @Test
    fun testLazyVal() {
        val lazyVal: CSLazyVal<String> = lazyVal { "value" }
        val value: String by lazyVal
        Assert.assertFalse(lazyVal.isInitialized)
        Assert.assertEquals("value", value)
        Assert.assertTrue(lazyVal.isInitialized)
    }
}