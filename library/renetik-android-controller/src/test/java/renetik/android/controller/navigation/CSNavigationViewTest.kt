package renetik.android.controller.navigation

import android.widget.FrameLayout
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
import renetik.android.core.lang.value.isTrue
import renetik.android.testing.TestApplication
import renetik.android.ui.R.layout.cs_frame_match

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSNavigationViewTest {

    private class Activity : CSActivity<CSActivityView<FrameLayout>>() {
        override fun createView() = ActivityView(this)
    }

    class ActivityView(activity: CSActivity<CSActivityView<FrameLayout>>) :
        CSActivityView<FrameLayout>(activity, layout(cs_frame_match)) {
        override var navigation: CSNavigationView? = CSNavigationView(this)
    }

    private val activityController: ActivityController<Activity> by lazy {
        buildActivity(Activity::class.java).setup()
    }
    private val navigation: CSNavigationView by lazy {
        activityController.get().activityView?.navigation!!
    }

    @Test
    fun test1() {
        assertTrue(navigation.isResume)
        val itemView = CSNavigationItemView(navigation, viewLayout = cs_frame_match).show()

        activityController.pause()
        assertTrue(navigation.isPaused)
        assertTrue(itemView.isPaused)

        itemView.dismiss()
        assertTrue(itemView.isDestructed)
    }

    @Test
    fun test2() {
        assertTrue(navigation.isResume)
        val itemView1 = CSNavigationItemView(navigation, viewLayout = cs_frame_match)
            .fullScreen().push()
        val itemView2 = CSNavigationItemView(navigation, viewLayout = cs_frame_match)
            .fullScreen().push()
        assertTrue(!itemView1.isVisible.isTrue)
        assertTrue(itemView2.isVisible.isTrue)
    }

    @Test
    fun test3() {
        assertTrue(navigation.isResume)
        val itemView1 = CSNavigationItemView(navigation, viewLayout = cs_frame_match)
            .fullScreen().push()
        val itemView2 = CSNavigationItemView(itemView1, viewLayout = cs_frame_match)
            .fullScreen().push()
        val itemView3 = CSNavigationItemView(itemView2, viewLayout = cs_frame_match)
            .fullScreen().push()
        assertTrue(!itemView1.isVisible.isTrue)
        assertTrue(!itemView2.isVisible.isTrue)
        assertTrue(itemView3.isVisible.isTrue)

        itemView2.dismiss()

        assertTrue(itemView1.isVisible.isTrue)
        assertTrue(itemView2.isDestructed)
        assertTrue(itemView3.isDestructed)
    }
}
