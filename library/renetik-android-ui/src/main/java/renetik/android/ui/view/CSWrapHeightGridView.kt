package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridView


// !!! Items in grid view has to have fixed height otherwise
// it doesn't compute right height for WRAP_CONTENT
class CSWrapHeightGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : GridView(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        Option 1
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        if (layoutParams.height == WRAP_CONTENT) {
//            super.onMeasure(widthMeasureSpec, makeMeasureSpec(MAX_VALUE shr 2, AT_MOST))
//        }

//        Option 2
//        val heightSpec = if (layoutParams.height == WRAP_CONTENT)
//            makeMeasureSpec(MAX_VALUE shr 2, AT_MOST)
//        else heightMeasureSpec
//        super.onMeasure(widthMeasureSpec, heightSpec)

        if (layoutParams.height == WRAP_CONTENT) {
            super.onMeasure(widthMeasureSpec, makeMeasureSpec(MEASURED_SIZE_MASK, AT_MOST))
//            val params = layoutParams
//            params.height = measuredHeight
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}