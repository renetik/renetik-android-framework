package renetik.android.ui.widget

import android.widget.TextView
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
import renetik.android.ui.view.isGone
import renetik.android.ui.view.isVisible

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class TextViewExtensionsTest {

    private val textView = TextView(context)

    @Test
    fun textSetAndGet() {
        textView.text("hello")
        assert(expected = "hello", actual = textView.text())
        textView.clearText()
        assert(expected = "", actual = textView.text())
    }

    @Test
    fun textAppendPrepend() {
        textView.text("middle")
        textView.textAppend(" end")
        assert(expected = "middle end", actual = textView.text())
        textView.textPrepend("start ")
        assert(expected = "start middle end", actual = textView.text())
    }

    @Test
    fun valueRendersAnyValue() {
        textView.value(42)
        assert(expected = "42", actual = textView.text())
    }

    @Test
    fun goneIfBlank() {
        textView.text("")
        textView.goneIfBlank()
        assertTrue(textView.isGone)
        textView.text("content")
        textView.goneIfBlank()
        assertTrue(textView.isVisible)
    }

    @Test
    fun textFollowsProperty() {
        val text = property("initial")
        val registration = textView.text(text)
        assert(expected = "initial", actual = textView.text())
        text assign "updated"
        assert(expected = "updated", actual = textView.text())
        registration.cancel()
        text assign "after cancel"
        assert(expected = "updated", actual = textView.text())
    }

    @Test
    fun onTextChangeNotifies() {
        var changes = 0
        textView.onTextChange { changes += 1 }
        textView.text("one")
        textView.text("two")
        assert(expected = 2, actual = changes)
    }
}
