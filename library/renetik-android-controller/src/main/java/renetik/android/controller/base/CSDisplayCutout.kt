package renetik.android.controller.base

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.DisplayCutout

@SuppressLint("NewApi")
class CSDisplayCutout(val native: DisplayCutout) {
    val leftRect: Rect?
        get() = if (!native.boundingRectLeft.isEmpty)
            native.boundingRectLeft else null
    val topRect: Rect?
        get() = if (!native.boundingRectTop.isEmpty)
            native.boundingRectTop else null
    val rightRect: Rect?
        get() = if (!native.boundingRectRight.isEmpty)
            native.boundingRectRight else null
    val bottomRect: Rect?
        get() = if (!native.boundingRectBottom.isEmpty)
            native.boundingRectBottom else null
}