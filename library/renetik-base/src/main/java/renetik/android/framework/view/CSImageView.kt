package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.ImageView
import renetik.android.R

class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes), CSHasTouchEvent {

    override val self = this
    private val _maxWidth: Int
    private val _maxHeight: Int
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            clipToOutline = attributes.getBoolean(R.styleable.CSLayout_clipToOutline, false)
            _maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth, -1)
            _maxHeight = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxHeight, -1)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec
        if (_maxWidth != -1 && measuredWidth > _maxWidth)
            widthMeasure = makeMeasureSpec(_maxWidth, EXACTLY)
        if (_maxHeight != -1 && measuredHeight > _maxHeight)
            heightMeasure = makeMeasureSpec(_maxHeight, EXACTLY)
        super.onMeasure(widthMeasure, heightMeasure)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.onTouchEvent(event) else true
    }
}