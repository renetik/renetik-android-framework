import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import renetik.android.ui.extensions.view.mediumAnimationDuration

open class CSCustomRecyclerSmoothScroller(
    context: Context, private val speedPerPixel: Float = 0.1f)
    : LinearSmoothScroller(context) {
    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float = speedPerPixel
}