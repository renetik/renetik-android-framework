package renetik.android.framework.sample

import android.view.View
import android.view.ViewGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = SampleApplication::class)
class ChecklistTest {

    @Test
    fun allModuleChecksPass() {
        val checks = ModuleChecks(context).all
        assertEquals(14, checks.size)
        val failed = checks.filter { !it.passed }.map { it.module }
        assertTrue("Failed module checks: $failed", failed.isEmpty())
    }

    @Test
    fun checklistIsAttachedToActivityWindowAndRowsRendered() {
        val activity = buildActivity(MainActivity::class.java).setup().get()
        val mainView = activity.activityView
        val root: ViewGroup = mainView.view

        // The navigation view must be a child of the activity root frame,
        // otherwise pushed screens are never in the window (blank screen).
        assertTrue("Navigation is not attached to the activity root frame",
            (0 until root.childCount).any { root.getChildAt(it) === mainView.navigation.view })

        // Every checklist row must be reachable from the activity root view
        // tree — not merely from the (possibly detached) checklist view.
        val checklist = mainView.checklist
        assertTrue(checklist.checks.isNotEmpty())
        assertTrue(checklist.allPassed)
        checklist.checks.forEach { check ->
            val status = root.findViewWithTag<View>(ChecklistView.statusTag(check))
            assertNotNull("Row for ${check.module} not in activity view tree", status)
        }
    }
}
