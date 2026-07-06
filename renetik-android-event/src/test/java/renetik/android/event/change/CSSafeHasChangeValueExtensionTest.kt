package renetik.android.event.change


import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks
import renetik.android.core.lang.variable.assign
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.CSSafeProperty.Companion.safe
import renetik.android.testing.CSAssert.assert

@RunWith(RobolectricTestRunner::class)
class CSSafeHasChangeValueExtensionTest {

    @Test
    fun hasUnsafeChangeValueKeepsUnsafePropagation() {
        val parent = CSModel()
        val source = property(1).safe(parent)
        val doubled = source.safeStateDelegate(parent, unsafeFromValue = { it * 2 })
        var unsafeValue: Int? = null
        var safeValue: Int? = null
        doubled.onUnsafeChange { unsafeValue = it }
        doubled.onChange { safeValue = it }

        Thread { source assign 2 }.apply {
            start()
            join()
        }
        assert(expected = 4, actual = doubled.value)
        assert(expected = 4, actual = unsafeValue)
        assert(expected = null, actual = safeValue)
        runUiThreadTasksIncludingDelayedTasks()
        assert(expected = 4, actual = safeValue)
    }
}
