package renetik.android.java.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnyCSNullTest {
    @Test
    fun notNull() {
        var a: String? = ""
        var b: String? = ""
        var c: String? = ""
        assertTrue(notNull(a, b, c))
        a = null
        assertFalse(notNull(a, b, c))
        b = null; c = null
        assertFalse(notNull(a, b, c))
    }

    @Test
    fun isNull() {
        var a: String? = ""
        var b: String? = ""
        var c: String? = ""
        assertFalse(isSomeNull(a, b, c))
        a = null
        assertTrue(isSomeNull(a, b, c))
        b = null; c = null
        assertTrue(isSomeNull(a, b, c))
    }
}