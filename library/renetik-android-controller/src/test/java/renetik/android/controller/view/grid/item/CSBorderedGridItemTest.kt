package renetik.android.controller.view.grid.item

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import renetik.android.controller.navigation.TestApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSBorderedGridItemTest {

    private val activity: ActivityController<Activity> by lazy {
        buildActivity(Activity::class.java).setup()
    }

    @Test
    fun testIsBottomBorder2Columns() {
        val item = object : CSBorderedGridItem {
            override val rightBorder: View = View(activity.get())
            override val bottomBorder: View = View(activity.get())
        }
        item.updateBorders(count = 7, columns = 2, index = 0, isBottomBorder = true)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)

        item.updateBorders(count = 7, columns = 2, index = 5, isBottomBorder = true)
        assertTrue(!item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)

        item.updateBorders(count = 7, columns = 2, index = 6, isBottomBorder = true)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)
    }

    @Test
    fun testNoBottomBorder2Columns() {
        val item = object : CSBorderedGridItem {
            override val rightBorder: View = View(activity.get())
            override val bottomBorder: View = View(activity.get())
        }
        item.updateBorders(count = 7, columns = 2, index = 0, isBottomBorder = false)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)

        item.updateBorders(count = 7, columns = 2, index = 5, isBottomBorder = false)
        assertTrue(!item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)

        item.updateBorders(count = 7, columns = 2, index = 6, isBottomBorder = false)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(!item.bottomBorder.isVisible)
    }

    @Test
    fun testNoBottomBorder2Columns2() {
        val item = object : CSBorderedGridItem {
            override val rightBorder: View = View(activity.get())
            override val bottomBorder: View = View(activity.get())
        }
        item.updateBorders(count = 4, columns = 2, index = 0, isBottomBorder = false)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(item.bottomBorder.isVisible)

        item.updateBorders(count = 4, columns = 2, index = 2, isBottomBorder = false)
        assertTrue(item.rightBorder.isVisible)
        assertTrue(!item.bottomBorder.isVisible)

        item.updateBorders(count = 4, columns = 2, index = 3, isBottomBorder = false)
        assertTrue(!item.rightBorder.isVisible)
        assertTrue(!item.bottomBorder.isVisible)
    }
}
