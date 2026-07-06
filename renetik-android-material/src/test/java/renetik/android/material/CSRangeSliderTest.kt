package renetik.android.material

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class CSRangeSliderTest {

    private val slider = CSRangeSlider(context)

    @Test
    fun valuesWithinRangeAndSteps() {
        slider.valueFrom = 0f
        slider.valueTo = 100f
        slider.stepSize = 10f
        slider.values = listOf(20f, 80f)
        assert(expected = listOf(20f, 80f), actual = slider.values)
    }

    @Test
    fun onChangeListenerFiresPerValueChange() {
        slider.valueFrom = 0f
        slider.valueTo = 1f
        var changes = 0
        slider.onChange { changes += 1 }
        slider.values = listOf(0.2f, 0.8f)
        assert(expected = 2, actual = changes)
    }
}
