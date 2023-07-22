package renetik.android.controller.navigation

import android.view.View
import android.view.ViewGroup
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.isPaused
import renetik.android.controller.extensions.push
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.ui.R.layout.cs_frame_match

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSNavigationViewTest {

    private class Activity : CSActivity() {
        override fun createView(): CSActivityView<out ViewGroup> = CSNavigationView(this)
    }

    private val activityController: ActivityController<Activity> by lazy {
        buildActivity(Activity::class.java).setup()
    }
    private val activity: Activity by lazy { activityController.get() }
    private val navigation by lazy { activity.activityView as CSNavigationView }

    @Test
    fun test1() {
        assertTrue(navigation.isResumed)
        val itemView = CSNavigationItemView<View>(navigation, cs_frame_match.layout).show()

        activityController.pause()
        assertTrue(navigation.isPaused)
        assertTrue(itemView.isPaused)

        itemView.dismiss()
        assertTrue(itemView.isDestructed)
    }

    @Test
    fun test2() {
        assertTrue(navigation.isResumed)
        val itemView1 = CSNavigationItemView<View>(navigation, cs_frame_match.layout)
            .fullScreen().push()
        val itemView2 = CSNavigationItemView<View>(navigation, cs_frame_match.layout)
            .fullScreen().push()
        assertTrue(!itemView1.isVisible)
        assertTrue(itemView2.isVisible)
    }

    @Test
    fun test3() {
        assertTrue(navigation.isResumed)
        val itemView1 = CSNavigationItemView<View>(navigation, cs_frame_match.layout)
            .fullScreen().push()
        val itemView2 = CSNavigationItemView<View>(itemView1, cs_frame_match.layout)
            .fullScreen().push()
        val itemView3 = CSNavigationItemView<View>(itemView2, cs_frame_match.layout)
            .fullScreen().push()
        assertTrue(!itemView1.isVisible)
        assertTrue(!itemView2.isVisible)
        assertTrue(itemView3.isVisible)

        itemView2.dismiss()

        assertTrue(itemView1.isVisible)
        assertTrue(itemView2.isDestructed)
        assertTrue(itemView3.isDestructed)
    }
}
