package renetik.android.ui.picker

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.variable.assign
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context
import renetik.android.ui.view.isVisible

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSNumberPickerTest {

    private val picker = CSNumberPicker(context)

    @Test
    fun valueSetAndGetWithinRange() {
        picker.min(0).max(10)
        picker.value = 7
        assert(expected = 7, actual = picker.value)
        assert(expected = 0, actual = picker.minValue)
        assert(expected = 10, actual = picker.maxValue)
    }

    @Test
    fun wheelConfiguration() {
        picker.circulate(true)
        assertTrue(picker.wrapSelectorWheel)
        picker.circulate(false)
        assertFalse(picker.wrapSelectorWheel)
    }

    @Test
    fun loadDisplaysDataAndSelection() {
        picker.load(listOf("alpha", "beta", "gamma"), selected = 1)
        assert(expected = 0, actual = picker.minValue)
        assert(expected = 2, actual = picker.maxValue)
        assert(expected = 3, actual = picker.displayedValues.size)
        assert(expected = "beta", actual = picker.displayedValues[picker.value])
        assertTrue(picker.isVisible)
    }

    @Test
    fun indexPropertySyncsToPicker() {
        picker.min(0).max(5)
        val selection = property(0)
        picker.index(selection)
        selection assign 3
        assert(expected = 3, actual = picker.value)
    }

    @Test
    fun loadWithPropertyUpdatesPickerSelection() {
        val choice = property("two")
        picker.load(choice, listOf("one" to "one", "two" to "two", "three" to "three"))
        assert(expected = 1, actual = picker.value)
    }
}
