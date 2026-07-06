package renetik.android.core.kotlin.primitives

import org.junit.Test
import renetik.android.testing.CSAssert.assert

class IntPlusPercentTest {

    @Test
    fun percentOfNumbers() {
        assert(expected = 0.79f, 1.percentOf(79f))
        assert(expected = 0.79, 1.percentOf(79.0))
        assert(expected = 0, 1.percentOfInt(79))
        assert(expected = 0, 1.percentOfInt(79f))
        assert(expected = 0, 1.percentOfInt(79.0))
    }

    @Test
    fun toPercentOfRange() {
        val range = 10..22000
        val valuePercent = 10.toPercentOf(range)
        assert(expected = 0f, valuePercent)
        assert(expected = 0f, valuePercent.percentOf(127f))
    }
}
