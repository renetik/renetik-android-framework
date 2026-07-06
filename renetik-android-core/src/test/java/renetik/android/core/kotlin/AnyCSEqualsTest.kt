package renetik.android.core.kotlin

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnyCSEqualsTest {
    @Test
    fun equalsAny() {
        assertTrue("third".equalsAny("first", "second", "third"))
        assertFalse("fourth".equalsAny("first", "second", "third"))

        val values = listOf("first", "second", "third")
        assertTrue("third" equalsAny values)
        assertFalse("fourth" equalsAny values)
    }

    @Test
    fun equalsNone() {
        assertTrue("fourth".equalsNone("first", "second", "third"))
        assertFalse("second".equalsNone("first", "second", "third"))

        val values = listOf("first", "second", "third")
        assertTrue("fourth" equalsNone values)
        assertFalse("first" equalsNone values)
    }

    @Test
    fun equalsAll() {
        assertTrue("fourth".equalsAll("fourth", "fourth", "fourth"))
        assertFalse("fourth".equalsAll("first", "second", "third"))

        assertTrue("fourth" equalsAll listOf("fourth", "fourth", "fourth"))
        assertFalse("fourth" equalsAll listOf("first", "second", "third"))
    }
}