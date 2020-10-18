package renetik.android.java.extensions

import org.junit.Assert.*
import org.junit.Test
import renetik.android.java.common.CSSizeInterface
import renetik.android.java.event.CSEventPropertyFunctions.property
import renetik.android.java.extensions.collections.list

class AnyCSSizeTest {
    @Test
    fun sizeTest() {
        assertEquals(0, null.size)
        assertEquals(5, 5.size)
        assertEquals(45, (45.4).size)
        assertEquals(1, (0.4).size)
        assertEquals(1, true.size)
        assertEquals(4, "test".size)
        assertEquals(2, (mapOf("a" to 1, "b" to 2) as Any).size)
        assertEquals(3, (list("a", "b", "c") as Any).size)
        assertEquals(10, property(10.00f).size)
        assertEquals(10, (CSSizeInterfaceTest("testString") as Any).size)
        assertEquals(3, (arrayOf("s", "S", 2) as Any).size)
        assertEquals(5, (intArrayOf(2, 3, 45, 5, 6) as Any).size)
    }

    @Test
    fun isEmptyTest() {
        assertTrue(null.isEmpty)
        assertTrue(0.isEmpty)
        assertTrue(0f.isEmpty)
        assertTrue(0.0.isEmpty)
        assertTrue(false.isEmpty)
        assertTrue("".isEmpty)
        assertTrue(emptyMap<String, String>().isEmpty)
        assertTrue(list<String>().isEmpty)
        assertTrue(property(0.00f).isEmpty)
        assertTrue(CSSizeInterfaceTest("").isEmpty)
        assertTrue(arrayOf<String>().isEmpty)
        assertTrue(intArrayOf().isEmpty)

        assertFalse(1.isEmpty)
        assertFalse(1f.isEmpty)
        assertFalse(0.5.isEmpty)
        assertFalse(true.isEmpty)
        assertFalse("sss".isEmpty)
        assertFalse(mapOf("a" to 1, "b" to 2).isEmpty)
        assertFalse(list("a", "b", "c").isEmpty)
        assertFalse(property(10.00f).isEmpty)
        assertFalse(CSSizeInterfaceTest("testString").isEmpty)
        assertFalse(arrayOf("s", "S", 2).isEmpty)
        assertFalse(intArrayOf(2, 3, 45, 5, 6).isEmpty)
    }
}

class CSSizeInterfaceTest(private val testString: String) : CSSizeInterface {
    override val size: Int get() = testString.size
}