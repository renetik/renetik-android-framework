import android.content.Context

class CSCenteringRecyclerSmoothScroller(
    context: Context, speedPerPixel: Float = 0.1f)
    : CSCustomRecyclerSmoothScroller(context, speedPerPixel) {
    override fun calculateDtToFit(
        viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int =
        boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
}

