package renetik.android.testing.ui.automator

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice

object CSDevice {
    val device: UiDevice get() = UiDevice.getInstance(getInstrumentation())
}