package renetik.android.core.kotlin.primitives

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class IntTest {

    @Test
    fun unique() {
        val value1 = Int.unique()
        val value2 = Int.unique()
        val value3 = Int.unique()
        val value4 = Int.unique()
        assertNotEquals(value1, value2)
        assertNotEquals(value2, value3)
        assertNotEquals(value3, value4)
        assertEquals(value1 + 1, value2)
        assertEquals(value2 + 1, value3)
        assertEquals(value3 + 1, value4)
    }
}
