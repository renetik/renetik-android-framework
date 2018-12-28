package renetik.android.java.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import renetik.android.java.extensions.StringBuilder
import renetik.android.java.extensions.cutStart
import renetik.android.java.extensions.deleteLast
import renetik.android.java.extensions.remove

class StringBuilderTest {
    @Test
    fun deleteLast() {
        val builder = StringBuilder("12345678")
        assertEquals("123456", builder.deleteLast(2).toString())
    }

    @Test
    fun cutStart() {
        val builder = StringBuilder("12345678")
        assertEquals("345678", builder.cutStart(2).toString())
    }

    @Test
    fun removeStartLength() {
        val builder = StringBuilder("12345678")
        assertEquals("125678", builder.remove(2, 2).toString())
    }
}
