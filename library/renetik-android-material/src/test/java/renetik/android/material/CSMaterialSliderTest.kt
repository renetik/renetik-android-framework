package renetik.android.material

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.variable.assign
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class CSMaterialSliderTest {

    private val slider = CSSlider(context)

    @Test
    fun valueIsClampedToRange() {
        slider.valueFrom(0).valueTo(10).stepSize(1)
        slider.value(15)
        assert(expected = 10f, actual = slider.value)
        slider.value(-5)
        assert(expected = 0f, actual = slider.value)
        slider.value(7)
        assert(expected = 7f, actual = slider.value)
    }

    @Test
    fun intPropertyTwoWayBinding() {
        val volume = property(30)
        slider.value(volume, min = 0, max = 100, step = 1)
        assert(expected = 30f, actual = slider.value)

        volume assign 60
        assert(expected = 60f, actual = slider.value)

        slider.value = 45f
        assert(expected = 45, actual = volume.value)
    }

    @Test
    fun floatPropertyBindingRoundsToStep() {
        val level = property(0.25f)
        slider.value(level, min = 0f, max = 1f, step = 0.1f)
        // 0.25 rounds to nearest 0.1 step
        assert(expected = 0.3f, actual = slider.value)

        level assign 0.71f
        assert(expected = 0.7f, actual = slider.value)
    }

    @Test
    fun onChangeListenerFires() {
        slider.valueFrom(0).valueTo(100).stepSize(1)
        var changes = 0
        slider.onChange { changes += 1 }
        slider.value = 10f
        slider.value = 20f
        assert(expected = 2, actual = changes)
    }
}
