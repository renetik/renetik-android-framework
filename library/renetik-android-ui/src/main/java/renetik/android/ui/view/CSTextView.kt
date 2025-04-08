package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.appcompat.widget.AppCompatTextView
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.ui.R

class CSTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), CSHasTouchEvent {

    override val self = this
    private val _maxHeight: Int
    private val dispatchState: Boolean
    override val eventOnTouch: CSEvent<CSTouchEventArgs> = event<CSTouchEventArgs>()

    init {
        clipToOutline = false
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
            _maxHeight = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxHeight, -1)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        if (minWidth != -1 && measuredWidth < minWidth) {
//            super.onMeasure(makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec)
//        } else if (maxWidth != -1 && measuredWidth > maxWidth) {
//            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
//        } else if (rotation == 270f || rotation == 90f)
//            super.onMeasure(heightMeasureSpec, widthMeasureSpec)

        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec

        if (_maxHeight != -1 && measuredHeight > _maxHeight)
            heightMeasure = makeMeasureSpec(_maxHeight, EXACTLY)

        if (rotation == 270f || rotation == 90f)
            super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        else super.onMeasure(widthMeasure, heightMeasure)
    }


    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchState) super.dispatchSetPressed(pressed)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false
        return if (processTouchEvent(event)) true else super.onTouchEvent(event)
    }
}