package renetik.android.ui.extensions.widget

import android.widget.TextView
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.variable.assign
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class TextViewExtTest {

    @Test
    fun `Verify how many times text is getting computed`() {
        val propertyInt = property(0)
        val propertyBool = property(false)
        val textView = TextView(context)
        var invocationCount = 0
        textView.text(propertyInt, propertyBool, text = { position, inBeat ->
            invocationCount += 1
        })
        assert(expected = 2, actual = invocationCount)
        propertyBool assign true
        assert(expected = 3, actual = invocationCount)
        propertyBool assign false
        assert(expected = 4, actual = invocationCount)
    }
}