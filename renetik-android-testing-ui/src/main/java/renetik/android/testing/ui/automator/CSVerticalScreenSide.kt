package renetik.android.testing.ui.automator

enum class CSVerticalScreenSide {
    Top, Bottom, Center;

    val y
        get() = when (this) {
            Center -> CSDevice.device.displayHeight / 2
            Top -> CSDevice.device.displayHeight / 4
            else -> CSDevice.device.displayHeight / 4 * 3
        }
}