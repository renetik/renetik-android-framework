package renetik.android.controller.view.grid.scroller

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

open class CSLinearSmoothScroller(
    context: Context, animationDuration: Int
) : LinearSmoothScroller(context) {
    private val millisecondsPerInch: Float = 1000f / animationDuration.toFloat()
    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float =
        millisecondsPerInch / displayMetrics.densityDpi
}