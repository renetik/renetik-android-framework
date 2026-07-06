package renetik.android.core.math

import org.junit.Assert.assertEquals
import org.junit.Test

class CSPointTest {

    @Test
    fun pointAliasesExposeCoordinates() {
        val point = CSPoint.point(3, 4)

        assertEquals(3, point.left)
        assertEquals(3, point.column)
        assertEquals(4, point.top)
        assertEquals(4, point.row)
    }

    @Test
    fun infixToCreatesPointAndMinusSubtractsCoordinates() {
        val point: CSPoint<Int> = 8 to 5

        assertEquals(CSPoint(8, 5), point)
        assertEquals(CSPoint(5, 1), point - CSPoint(3, 4))
        assertEquals(CSPoint(1.5f, 2.5f), CSPoint(5f, 7f) - CSPoint(3.5f, 4.5f))
    }
}
