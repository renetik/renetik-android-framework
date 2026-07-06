package renetik.android.core.kotlin.collections

import org.junit.Assert.assertEquals
import org.junit.Test

class ListRangeTest {

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    @Test
    fun rangeFromTest() {
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list.rangeFrom { it < 5 })
        assertEquals(listOf(6, 7, 8, 9, 10), list.rangeFrom { it > 5 })
        assertEquals(list, list.rangeFrom { it > -5 })
        assertEquals(emptyList<Int>(), list.rangeFrom { it > 10 })
        assertEquals(list, list.rangeFrom { it < 5 })
    }

    @Test
    fun rangeToTest() {
        assertEquals(listOf(1, 2, 3, 4), list.rangeTo { it < 5 })
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list.rangeTo { it > 5 })
        assertEquals(emptyList<Int>(), list.rangeTo { it < 1 })
        assertEquals(emptyList<Int>(), list.rangeTo { it < 1 })
    }

    @Test
    fun rangeTest() {
        assertEquals(listOf(3, 4, 5),
            list.range(from = { it > 2 }, to = { it < 6 }))

        assertEquals(listOf(7, 8, 9, 10),
            list.range(from = { it > 6 }, to = { it < 15 }))

        assertEquals(listOf(1, 2, 3),
            list.range(from = { it > -5 }, to = { it < 4 }))

        assertEquals(emptyList<Int>(),
            list.range(from = { it < -5 }, to = { it > 4 }))

        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            list.range(from = { it < 5 }, to = { it > 7 }))
    }
}