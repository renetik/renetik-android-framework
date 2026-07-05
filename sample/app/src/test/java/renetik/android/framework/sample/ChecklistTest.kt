package renetik.android.framework.sample

import org.junit.Assert.assertEquals
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
    fun checklistScreenShowsAllRowsPassed() {
        val activity = buildActivity(MainActivity::class.java).setup().get()
        val checklist = activity.activityView!!.checklist
        assertTrue(checklist.checks.isNotEmpty())
        assertTrue(checklist.allPassed)
        // every check row rendered its status view with the passed tag
        checklist.checks.forEach { check ->
            val status = checklist.view.findViewWithTag<android.view.View>(
                ChecklistView.statusTag(check))
            assertTrue("Row for ${check.module} not rendered", status != null)
            assertTrue("Row for ${check.module} not passed", check.passed)
        }
    }
}
