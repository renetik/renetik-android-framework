package renetik.android.testing.ui.automator

import renetik.android.testing.ui.automator.CSDevice.device

enum class CSHorizontalScreenSide {
    Left, Right, Center;

    val x
        get() = when (this) {
            Center -> device.centerScreenX
            Left -> device.displayWidth / 4
            else -> device.displayWidth / 4 * 3
        }
}

