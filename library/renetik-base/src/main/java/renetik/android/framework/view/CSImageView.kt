package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.ImageView
import renetik.android.R


open class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {

    private val _maxHeight: Int

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            _maxHeight = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxHeight, -1)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (_maxHeight != -1 && measuredHeight > _maxHeight)
            super.onMeasure(widthMeasureSpec, makeMeasureSpec(_maxHeight, EXACTLY))
    }
}