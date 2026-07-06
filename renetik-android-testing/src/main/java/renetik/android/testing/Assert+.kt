package renetik.android.testing

import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue

object CSAssert {
    fun assertThrows(function: () -> Unit) = assertThrows(Exception::class.java, function)
    fun assertFail(function: () -> Unit) = assertThrows(function)
    fun assertContains(actual: String, vararg values: String) {
        values.forEach { assertTrue("$actual don't contains $it", it in actual) }
    }
    fun assertContainsNot(actual: String, vararg values: String) {
        values.forEach { assertTrue("$actual contains $it", it !in actual) }
    }

    fun assert(expected: Any?, actual: Any?) = Assert.assertEquals(expected, actual)
    fun assert(expected: Boolean) = Assert.assertTrue(expected)
}

