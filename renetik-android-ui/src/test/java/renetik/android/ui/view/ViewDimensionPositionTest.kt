package renetik.android.ui.view

import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewDimensionPositionTest {

    private fun viewWithParams() = View(context).apply {
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    @Test
    fun sizeUpdatesLayoutParams() {
        val view = viewWithParams()
        view.size(width = 100, height = 50)
        assert(expected = 100, actual = view.layoutWidth)
        assert(expected = 50, actual = view.layoutHeight)
        view.size(25)
        assert(expected = 25, actual = view.layoutWidth)
        assert(expected = 25, actual = view.layoutHeight)
    }

    @Test
    fun matchParentAndWrapContent() {
        val view = viewWithParams()
        view.matchParent()
        assert(expected = MATCH_PARENT, actual = view.layoutWidth)
        assert(expected = MATCH_PARENT, actual = view.layoutHeight)
        view.wrapContent()
        assert(expected = WRAP_CONTENT, actual = view.layoutWidth)
        assert(expected = WRAP_CONTENT, actual = view.layoutHeight)
    }

    @Test
    fun marginsUpdateLayoutParams() {
        val view = viewWithParams()
        view.margins(left = 1, top = 2, right = 3, bottom = 4)
        val params = view.layoutParams as MarginLayoutParams
        assert(expected = 1, actual = params.leftMargin)
        assert(expected = 2, actual = params.topMargin)
        assert(expected = 3, actual = params.rightMargin)
        assert(expected = 4, actual = params.bottomMargin)

        view.margin(7)
        assert(expected = 7, actual = view.topMargin)
        assert(expected = 7, actual = view.startMargin)
        assert(expected = 7, actual = view.endMargin)

        view.topMargin = 11
        assert(expected = 11, actual = view.topMargin)
        assert(expected = 7, actual = view.startMargin)
    }

    @Test
    fun hasSizeReflectsMeasuredLayout() {
        val view = View(context)
        assertFalse(view.hasSize)
        val parent = FrameLayout(context)
        parent.addView(view, MarginLayoutParams(120, 60))
        parent.measure(
            View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY))
        parent.layout(0, 0, 500, 500)
        assertTrue(view.hasWidth)
        assertTrue(view.hasHeight)
        assertTrue(view.hasSize)
    }

    @Test
    fun centerReflectsLayoutPosition() {
        val parent = FrameLayout(context)
        val view = View(context)
        parent.addView(view, MarginLayoutParams(100, 40))
        parent.measure(
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY))
        parent.layout(0, 0, 200, 200)
        assert(expected = 50, actual = view.center.x)
        assert(expected = 20, actual = view.center.y)
    }
}
