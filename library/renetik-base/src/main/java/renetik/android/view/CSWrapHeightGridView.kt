package renetik.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.AbsListView.LayoutParams.WRAP_CONTENT
import android.widget.GridView
import kotlin.Int.Companion.MAX_VALUE

class CSWrapHeightGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : GridView(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec = if (layoutParams.height == WRAP_CONTENT)
            makeMeasureSpec(MAX_VALUE shr 2, AT_MOST)
        else heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}