package renetik.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.widget.LinearLayout
import renetik.android.R


class CSLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val maxWidth: Int

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CSLinearLayout, 0, 0)
        try {
            maxWidth = typedArray.getDimensionPixelSize(R.styleable.CSLinearLayout_maxWidth, 0)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > maxWidth) {
            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
        }
    }

}