package renetik.android.java.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringBuilderTest {
    @Test
    fun deleteLast() =
        assertEquals("123456", StringBuilder("12345678").deleteLast(2).toString())

    @Test
    fun cutStart() =
        assertEquals("345678", StringBuilder("12345678").cutStart(2).toString())

    @Test
    fun removeStartLength() =
        assertEquals("125678", StringBuilder("12345678").remove(2, 2).toString())
}
