package renetik.android.controller.view.grid

import android.content.Context
import renetik.android.ui.extensions.view.mediumAnimationDuration

class CSCenteringRecyclerSmoothScroller(
    context: Context, animationDuration: Int = mediumAnimationDuration
) : CSCustomRecyclerSmoothScroller(context, animationDuration) {
    override fun calculateDtToFit(
        viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int
    ): Int = boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
}

