import android.content.Context
import renetik.android.ui.extensions.view.mediumAnimationDuration

class CSCenteringRecyclerSmoothScroller(
    context: Context, speedPerPixel: Int = mediumAnimationDuration
) : CSCustomRecyclerSmoothScroller(context, speedPerPixel / 1000f) {
    override fun calculateDtToFit(
        viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int
    ): Int = boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
}

