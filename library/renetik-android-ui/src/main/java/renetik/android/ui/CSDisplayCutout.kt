package renetik.android.ui

import android.annotation.TargetApi
import android.graphics.Rect
import android.view.DisplayCutout
import renetik.android.core.android.util.orientedWidthPixels
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.extensions.content.displayMetrics

@TargetApi(28)
class CSDisplayCutout(private val cutout: DisplayCutout) {
    val safeLeft get() = cutout.safeInsetLeft
    val safeRight get() = cutout.safeInsetRight
    val safeInsetTop get() = cutout.safeInsetTop
    val safeInsetBottom get() = cutout.safeInsetBottom

    @TargetApi(29)
    fun leftRect(): Rect? = if (!cutout.boundingRectLeft.isEmpty)
        cutout.boundingRectLeft else null

    @TargetApi(29)
    fun topRect(): Rect? = if (!cutout.boundingRectTop.isEmpty)
        cutout.boundingRectTop else null

    @TargetApi(29)
    fun rightRect(): Rect? = if (!cutout.boundingRectRight.isEmpty)
        cutout.boundingRectRight else null

    @TargetApi(29)
    fun bottomRect(): Rect? = if (!cutout.boundingRectBottom.isEmpty)
        cutout.boundingRectBottom else null

    val isHolePunchCutout: Boolean
        get() {
            val bounds = cutout.boundingRects
            if (bounds.size == 1) {
                val boundingRect = bounds[0]
                if (boundingRect.isRoughlyCircular) {
                    val diameter = boundingRect.width().coerceAtMost(boundingRect.height())
                    val holePunchThreshold = app.displayMetrics.orientedWidthPixels * 0.15
                    return diameter < holePunchThreshold
                }
            }
            return false
        }

    private val Rect.isRoughlyCircular: Boolean
        get() = width().toFloat() / height().toFloat() in 0.8..1.4
}