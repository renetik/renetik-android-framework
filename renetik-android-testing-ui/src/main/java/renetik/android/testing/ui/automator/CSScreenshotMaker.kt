package renetik.android.testing.ui.automator

import renetik.android.core.android.content.externalFilesDir
import renetik.android.core.java.io.recreateDirs
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.testing.ui.automator.CSDevice.device
import renetik.android.testing.ui.espresso.CSEspresso.context
import java.io.File

class CSScreenshotMaker(dir: String = "screenshots") {

    private val imageDir = File(context.externalFilesDir, dir).recreateDirs()
    private var count = 0
    private var enabled = true

    fun capture(title: String, wait: Int = 1, language: String) {
        if (!enabled) return
        logInfo { "capture: $title" }
        if (wait > 0) device.waitForMoreIdle(milliseconds = 1000)
        count++
        val prefix = if (count < 10) "00" else if (count < 100) "0" else ""
        val name = "${language}_$prefix$count-$title"
        device.takeScreenshot(File(imageDir, "$name.png"))
        if (wait > 0) device.waitForMoreIdle()
    }

    fun resetCounter() {
        count = 0
    }

    fun disable() {
        enabled = false
    }
}