package renetik.android.core.lang

import org.junit.Assert
import org.junit.Test
import renetik.android.core.lang.lazy.CSLazyNullableVar
import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar

class CSLazyNullableVarTest {
    @Test
    fun testLazyNullableVar() {
        val lazyNullableVar: CSLazyNullableVar<String?> = lazyNullableVar { "initial" }
        var testVar: String? by lazyNullableVar
        Assert.assertEquals("initial", testVar)
        testVar = "test"
        Assert.assertEquals("test", testVar)
        testVar = null
        Assert.assertEquals(null, testVar)
    }
}