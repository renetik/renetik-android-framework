package renetik.android.controller.base

import android.annotation.TargetApi
import android.graphics.Rect
import android.view.DisplayCutout

@TargetApi(28)
class CSDisplayCutout(private val native: DisplayCutout) {
    val safeLeft get() = native.safeInsetLeft
    val safeRight get() = native.safeInsetRight
    val safeInsetTop get() = native.safeInsetTop
    val safeInsetBottom get() = native.safeInsetBottom

    @TargetApi(29)
    fun leftRect(): Rect? = if (!native.boundingRectLeft.isEmpty)
        native.boundingRectLeft else null

    @TargetApi(29)
    fun topRect(): Rect? = if (!native.boundingRectTop.isEmpty)
        native.boundingRectTop else null

    @TargetApi(29)
    fun rightRect(): Rect? = if (!native.boundingRectRight.isEmpty)
        native.boundingRectRight else null

    @TargetApi(29)
    fun bottomRect(): Rect? = if (!native.boundingRectBottom.isEmpty)
        native.boundingRectBottom else null
}