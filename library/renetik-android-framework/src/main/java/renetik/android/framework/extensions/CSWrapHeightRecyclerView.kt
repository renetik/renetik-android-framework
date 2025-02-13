package renetik.android.framework.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.recyclerview.widget.RecyclerView

class CSWrapHeightRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandedSpec = makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, expandedSpec)
    }
}