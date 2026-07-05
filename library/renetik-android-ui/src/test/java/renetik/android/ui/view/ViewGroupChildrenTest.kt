package renetik.android.ui.view

import android.view.View
import android.widget.FrameLayout
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewGroupChildrenTest {

    private val group = FrameLayout(context)

    @Test
    fun addRemoveAndIndexing() {
        val first = View(context)
        val second = View(context)
        group.add(first)
        group.add(second)
        assert(expected = 2, actual = group.childCount)
        assertSame(first, group[0])
        assertSame(second, group[1])
        assertSame(first, group.firstChild)
        assertSame(second, group.lastChild)
        assert(expected = 1, actual = group.lastIndex)

        group.remove(first)
        assert(expected = 1, actual = group.childCount)
        assertSame(second, group.firstChild)
    }

    @Test
    fun addAtIndex() {
        val first = View(context)
        val second = View(context)
        val inserted = View(context)
        group.add(first)
        group.add(second)
        group.add(inserted, index = 1)
        assertSame(first, group[0])
        assertSame(inserted, group[1])
        assertSame(second, group[2])
    }

    @Test
    fun removeAtReturnsRemovedChild() {
        val first = View(context)
        val second = View(context)
        group.add(first)
        group.add(second)
        val removed = group.removeAt(0)
        assertSame(first, removed)
        assert(expected = 1, actual = group.childCount)
        assertSame(second, group.firstChild)
    }

    @Test
    fun clearRemovesEverything() {
        repeat(3) { group.add(View(context)) }
        assert(expected = 3, actual = group.childCount)
        group.clear()
        assert(expected = 0, actual = group.childCount)
        assertNull(group.firstChild)
        assertNull(group.lastChild)
    }

    @Test
    fun subViewsListsChildren() {
        val first = View(context)
        val second = View(context)
        group.add(first)
        group.add(second)
        assert(expected = listOf<View>(first, second), actual = group.subViews)
    }
}
