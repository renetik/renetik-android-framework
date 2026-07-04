package renetik.android.testing.ui.espresso

import android.app.Activity
import android.app.Application
import android.view.InputDevice.SOURCE_UNKNOWN
import android.view.MotionEvent.BUTTON_PRIMARY
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press.FINGER
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables.breadthFirstViewTraversal
import org.hamcrest.Matcher
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.primitives.second
import renetik.android.testing.ui.automator.CSDevice.device
import renetik.android.testing.ui.automator.waitForMoreIdle
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeoutException
import kotlin.random.Random

object CSEspresso {
    private const val waitTime: Long = 100

    val context: Application get() = ApplicationProvider.getApplicationContext()

    inline fun <reified A : Activity> launch(): ActivityScenario<A> =
        ActivityScenario.launch(A::class.java)

    fun randomClick(isLong: Boolean = false) {
        runCatching {
            onView(isRoot()).perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> = isRoot()
                override fun getDescription(): String = "Performing a random click"
                override fun perform(uiController: UiController, view: View) {
                    val x = Random.nextInt(view.width)
                    val y = Random.nextInt(view.height)
                    runCatching {
                        GeneralClickAction(
                            if (isLong) Tap.LONG else Tap.SINGLE,
                            { floatArrayOf(x.toFloat(), y.toFloat()) },
                            FINGER, SOURCE_UNKNOWN, BUTTON_PRIMARY
                        ).perform(uiController, view)
                    }
                }
            })
        }
        device.waitForMoreIdle()
    }

    fun randomSwipe() {
        runCatching {
            onView(isRoot()).perform(listOf(
                swipeDown(), swipeUp(), swipeRight(), swipeLeft()
            ).random())
        }
        device.waitForMoreIdle()
    }

    fun checkTextDisplayed(@StringRes text: Int, timeout: Int = 5.second) =
        checkTextDisplayed(context.getString(text), timeout)

    fun checkTextDisplayed(text: String, timeout: Int = 5.second) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() = "checkTextDisplayed:$text timeout:$timeout"
            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                do {
                    for (child in breadthFirstViewTraversal(root))
                        if ((child as? TextView)?.text == text) {
                            ui.waitForIdle()
                            return
                        }
                    ui.loopMainThreadForAtLeast(100)
                } while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    fun isTextDisplayed(text: String, timeout: Int = 3.second): Boolean {
        device.waitForMoreIdle()
        var result = false
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() = "isTextDisplayed:$text timeout:$timeout"
            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                do {
                    for (child in breadthFirstViewTraversal(root))
                        if ((child as? TextView)?.text == text) {
                            result = true
                            ui.waitForIdle()
                            return
                        }
                    ui.loopMainThreadForAtLeast(100)
                } while (currentTimeMillis() < endTime)
                return
            }
        })
        return result
    }

    fun isViewInLayout(@IdRes viewId: Int, timeout: Int = 2.second) = try {
        waitForViewInLayout(viewId, timeout)
        true
    } catch (ex: Throwable) {
        false
    }

    fun wait(timeout: Int = 3.second) {
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() = "wait:$timeout"
            override fun perform(ui: UiController, root: View) {
                val endTime = currentTimeMillis() + timeout
                do ui.loopMainThreadForAtLeast(100)
                while (currentTimeMillis() < endTime)
                ui.waitForIdle()
            }
        })
    }


    fun waitForViewInLayout(@IdRes viewId: Int, timeout: Int = 7.second) =
        getView(viewId, index = 0, timeout) {}

    fun getView(@IdRes viewId: Int, timeout: Int = 7.second, function: (View) -> Unit) =
        getView(viewId, index = 0, timeout, function)

    fun getView(@IdRes viewId: Int, where: (View) -> Boolean,
                timeout: Int = 7.second, function: (View) -> Unit) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do
                    root.getChild(viewMatcher, where)?.let {
                        ui.loopMainThreadForAtLeast(100)
                        function(it)
                        ui.loopMainThreadForAtLeast(100)
                        return
                    } ?: run { ui.loopMainThreadForAtLeast(100) }
                while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    fun getAllViews(@IdRes viewId: Int, where: (View) -> Boolean = { true },
                    timeout: Int = 7.second, function: (List<View>) -> Unit) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do
                    root.getChilds(viewMatcher, where)?.let {
                        function(it)
                        ui.waitForIdle()
                        return
                    } ?: run { ui.loopMainThreadForAtLeast(100) }
                while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    fun <Value> getValue(
        @IdRes viewId: Int, timeout: Int = 7.second,
        function: (View) -> Value): Value =
        getValue(viewId, index = 0, timeout, function)

    fun getView(@IdRes viewId: Int, index: Int,
                timeout: Int = 7.second, function: (View) -> Unit) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do {
                    root.getChild(viewMatcher, index)?.let {
                        function(it)
                        ui.waitForIdle()
                        return
                    }
                    ui.loopMainThreadForAtLeast(100)
                } while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    fun waitFor(@IdRes viewId: Int, index: Int = 0,
                timeout: Int = 7.second, function: (View) -> Boolean) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do {
                    root.getChild(viewMatcher, index)?.let {
                        if (function(it)) {
                            ui.waitForIdle()
                            return
                        }
                    }
                    ui.loopMainThreadForAtLeast(100)
                } while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    fun <Value> getValue(
        @IdRes viewId: Int, index: Int = 0, timeout: Int = 7.second,
        function: (View) -> Value): Value {
        device.waitForMoreIdle()
        var value: Value? = null
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do
                    root.getChild(viewMatcher, index)?.let {
                        value = function(it)
                        ui.waitForIdle()
                        return
                    } ?: run { ui.loopMainThreadForAtLeast(100) }
                while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
        return value!!
    }

    fun <Value> getValue(
        @IdRes viewId: Int, where: (View) -> Boolean = { true },
        timeout: Int = 7.second, function: (View) -> Value): Value {
        device.waitForMoreIdle()
        var value: Value? = null
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("getView", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val viewMatcher = withId(viewId)
                do
                    root.getChild(viewMatcher, where)?.let {
                        value = function(it)
                        ui.waitForIdle()
                        return
                    } ?: run { ui.loopMainThreadForAtLeast(100) }
                while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
        return value!!
    }

    private fun UiController.waitForIdle() {
        loopMainThreadForAtLeast(waitTime)
        loopMainThreadUntilIdle()
        loopMainThreadForAtLeast(waitTime)
        loopMainThreadUntilIdle()
    }

    fun waitForViewNotInLayout(@IdRes viewId: Int, timeout: Int = 7.second) {
        device.waitForMoreIdle()
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() =
                viewIdTimeoutDescription("waitForViewNotInLayout", viewId, timeout)

            override fun perform(ui: UiController, root: View) {
                ui.waitForIdle()
                val endTime = currentTimeMillis() + timeout
                val matcher = withId(viewId)
                do
                    if (root.hasChild(matcher))
                        ui.loopMainThreadForAtLeast(100)
                    else {
                        ui.waitForIdle()
                        return
                    }
                while (currentTimeMillis() < endTime)
                throwTimeoutException(description, root)
            }
        })
    }

    private fun viewIdTimeoutDescription(
        title: String, viewId: Int, timeout: Int): String {
        val viewIdName = context.resources.getResourceEntryName(viewId);
        return "$title:$viewIdName timeout:$timeout"
    }

    fun View.hasChild(childMatcher: Matcher<View>) = getChild(childMatcher) != null

    fun View.getChild(childMatcher: Matcher<View>): View? {
        for (child in breadthFirstViewTraversal(this))
            if (childMatcher.matches(child)) return child
        return null
    }


    fun View.getChild(childMatcher: Matcher<View>, index: Int): View? {
        var childIndex = 0
        for (child in breadthFirstViewTraversal(this))
            if (childMatcher.matches(child)) {
                if (childIndex == index) return child
                childIndex++
            }
        return null
    }

    fun View.getChild(childMatcher: Matcher<View>, where: (View) -> Boolean): View? {
        for (child in breadthFirstViewTraversal(this))
            if (childMatcher.matches(child) && where(child)) return child
        return null
    }

    fun View.getChilds(childMatcher: Matcher<View>,
                       where: (View) -> Boolean): List<View>? {
        val list = mutableListOf<View>()
        for (child in breadthFirstViewTraversal(this))
            if (childMatcher.matches(child) && where(child)) list.add(child)
        return if (list.hasItems) list else null
    }

    private fun throwTimeoutException(description: String, view: View) {
        throw PerformException.Builder().withCause(TimeoutException())
            .withActionDescription(description)
            .withViewDescription(HumanReadables.describe(view)).build()
    }
}