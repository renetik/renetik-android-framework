@file:Suppress("unused")

package renetik.android.testing.ui.automator

import android.os.SystemClock.sleep
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.primitives.second
import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.testing.ui.automator.CSHorizontalScreenSide.Center
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

const val defaultWaitTime = 2
const val defaultTimeOut = 10 * Second

fun UiDevice.wait(time: Duration = 1.seconds) {
    runBlocking { delay(time) }
    waitForMoreIdle()
}

fun UiDevice.pressBackAndWait() {
    logInfo { "pressBackAndWait" }
    waitForMoreIdle(repeat = 1)
    pressBack()
    waitForMoreIdle()
}

fun UiDevice.waitForMoreIdle(repeat: Int = 2, milliseconds: Long) =
    waitForMoreIdle(repeat, milliseconds.milliseconds)

fun UiDevice.waitForMoreIdle(repeat: Int = 2, wait: Duration = 200.milliseconds) {
    logInfo { "waitForMoreIdle times:$repeat" }
    repeat(repeat) {
        sleep(wait.inWholeMilliseconds)
        waitForIdle()
    }
}

fun UiDevice.waitForNotVisible(description: String, timeout: Int = defaultTimeOut) {
    logInfo { "waitForNotVisible:$description" }
    wait(Until.gone(By.desc(description)), timeout.toLong())
    waitForMoreIdle()
}

fun UiDevice.waitForVisible(description: String, timeout: Int = defaultTimeOut) {
    logInfo { "waitForVisible:$description" }
    waitFor(By.desc(description), timeout)
}

fun UiDevice.waitForText(text: String, timeout: Int = defaultTimeOut) {
    logInfo { "waitForText:$text" }
    waitFor(By.text(text), timeout)
}

fun UiDevice.isVisible(description: String, timeout: Int = 3): Boolean {
    logInfo { "isVisible description:$description, timeout:$timeout" }
    val selector = By.desc(description)
    waitFor(selector, timeout)
    return hasObject(selector)
}

fun UiDevice.isVisibleText(text: String, timeout: Int = 3): Boolean {
    logInfo { "isVisible text:$text, timeout:$timeout" }
    val selector = By.text(Pattern.compile(text, CASE_INSENSITIVE))
    waitFor(selector, timeout)
    return hasObject(selector)
}

fun UiDevice.touchScreen(
    horizontal: CSHorizontalScreenSide = Center,
    vertical: CSVerticalScreenSide = CSVerticalScreenSide.Center,
) = press(horizontal.x, vertical.y)

fun UiDevice.press(x: Int, y: Int, time: Int = 50) {
    logInfo { "press x:$x, y:$y, time:$time" }
    waitForMoreIdle()
    swipe(x, y, x, y, time / 5)
}

fun UiDevice.swipeDown(
    times: Int = 1, marginVertical: Int = 50,
    side: CSHorizontalScreenSide = Center,
) {
    logInfo { "swipeDown times:$times, marginVertical:$marginVertical, side:$side" }
    waitForMoreIdle()
    repeat(times) {
        swipe(side.x, marginVertical, side.x, displayHeight - marginVertical, 20)
    }
}

fun UiDevice.swipeUp(
    times: Int = 1, marginVertical: Int = 50,
    side: CSHorizontalScreenSide = Center,
) {
    logInfo { "swipeUp times:$times, marginVertical:$marginVertical, side:$side" }
    waitForMoreIdle()
    repeat(times) {
        swipe(side.x, displayHeight - marginVertical, side.x, marginVertical, 20)
    }
    waitForMoreIdle()
}

fun UiDevice.findByDesc(description: String): UiObject2? {
    logInfo { "findByDesc:$description" }
    return findObjects(waitFor(By.desc(description), 2.second)).firstOrNull()
}

fun UiDevice.findAllByDesc(
    description: String, timeout: Int = defaultTimeOut
): List<UiObject2> {
    logInfo { "findAllByDesc:$description" }
    return findObjects(waitFor(By.desc(description), timeout))
}

fun UiDevice.clickByText(text: String, timeout: Int = defaultTimeOut): Unit? {
    logInfo { "clickByText:$text" }
    val toClick: UiObject2? = findObject(waitFor(By.text(text), timeout))
    return runCatching {
        toClick?.click() ?: logWarn { "not found clickByText:$text" }
        waitForMoreIdle()
    }.getOrNull()
}

fun UiDevice.clickByDesc(description: String, timeout: Int = defaultTimeOut) {
    //    displayMessage(description)
    logInfo { "clickByDesc:$description" }
    val toClick: UiObject2? = findObject(waitFor(By.desc(description), timeout))
    runCatching { toClick?.click() ?: logWarn { "not found clickByDesc:$description" } }
    waitForMoreIdle()
}

fun UiDevice.clickByDesc(
    description: String, index: Int = 0, timeout: Int = defaultTimeOut
) {
//    displayMessage(description)
    logInfo { "clickByDesc:$description index:$index" }
    findAllByDesc(description, timeout).at(index)?.click()
    waitForMoreIdle()
}

fun UiDevice.clickByDesc(
    description: String, duration: Int = 100, index: Int = 0,
    timeout: Int = defaultTimeOut
) {
    logInfo { "clickByDesc:$description duration:$duration index:$index" }
    findAllByDesc(description, timeout).at(index)?.click(duration.toLong())
//    displayMessage(description)
    waitForMoreIdle()
}

fun UiDevice.clickById(id: String) {
    logInfo { "clickById:$id" }
    waitForMoreIdle()
    findObject(UiSelector().resourceId(id)).click()
    waitForMoreIdle()
}

fun UiDevice.countByDesc(description: String): Int {
    logInfo { "countByDesc:$description" }
    return findObjects(waitFor(By.desc(description))).size
}

private fun UiDevice.waitForClickable(
    selector: BySelector, timeout: Int = defaultTimeOut,
): BySelector {
    waitFor(selector.clickable(true), timeout)
    return selector
}

fun UiDevice.waitFor(
    selector: BySelector,
    timeout: Int = defaultTimeOut
): BySelector {
    wait(Until.hasObject(selector), timeout.toLong())
    waitForMoreIdle()
    return selector
}

fun UiDevice.clearTasks(swipes: Int = 2) {
    logInfo { "clearTasks swipes:$swipes" }
    pressHome()
    waitForMoreIdle()
    pressRecentApps()
    waitForMoreIdle()
    repeat(swipes) {
        swipe(centerScreenX, centerScreenY, centerScreenX, 0, 10)
        waitForMoreIdle()
    }
    repeat(swipes) {
        swipe(centerScreenX, centerScreenY, 0, centerScreenY, 10)
        waitForMoreIdle()
    }
}

val UiDevice.centerScreenX get() = displayWidth / 2
val UiDevice.centerScreenY get() = displayHeight / 2