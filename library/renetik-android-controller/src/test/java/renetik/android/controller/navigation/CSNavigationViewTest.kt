package renetik.android.controller.navigation

import android.view.ViewGroup
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
import renetik.android.ui.R.layout.cs_frame_match

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSNavigationViewTest {

    private class Activity : CSActivity() {
        override fun createView(): CSActivityView<out ViewGroup> = CSNavigationView(this)
    }

    private val controller: ActivityController<Activity> by lazy {
        buildActivity(Activity::class.java).setup()
    }
    private val activity: Activity by lazy { controller.get() }
    private val navigation by lazy { activity.activityView as CSNavigationView }

    @Test
    fun test() {
        assertTrue(navigation.isResumed)
        val item = CSNavigationItemView<FrameLayout>(navigation, layout(cs_frame_match)).push()

        controller.pause()
        assertTrue(navigation.isPaused)
        assertTrue(item.isPaused)

        navigation.pop()
        assertTrue(item.isDestructed)
    }
}
