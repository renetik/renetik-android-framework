package renetik.android.core.lang.value

import org.junit.Test
import renetik.android.testing.CSAssert

class CSValueTest {
    @Test
    fun testIsNull() {
        val value1: CSValue<Int?> = CSValue.value(1)
        CSAssert.assert(expected = false, value1.isNull)
        val value2: CSValue<Int?> = CSValue.value(null)
        CSAssert.assert(expected = true, value2.isNull)
//        val value3: CSValue<Int> = CSValue.value(2)
//        CSAssert.assert(expected = false, value3.isNull) // wont compile
    }

    @Test
    fun testnotNull() {
        val value1: CSValue<Int?> = CSValue.value(1)
        CSAssert.assert(expected = true, value1.notNull)
        val value2: CSValue<Int?> = CSValue.value(null)
        CSAssert.assert(expected = false, value2.notNull)
//        val value3: CSValue<Int> = CSValue.value(2)
//        CSAssert.assert(expected = true, value3.notNull) // wont compile
    }
}