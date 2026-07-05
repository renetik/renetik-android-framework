package renetik.android.framework.sample

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChecklistUiTest {

    @Test
    fun checklistLaunchesAndAllModulesPass() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val checklist = activity.activityView!!.checklist
                val failed = checklist.checks.filter { !it.passed }.map { it.module }
                assertTrue("Failed module checks: $failed", failed.isEmpty())
            }
        }
    }
}
