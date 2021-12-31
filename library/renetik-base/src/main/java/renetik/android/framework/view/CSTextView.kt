package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.appcompat.widget.AppCompatTextView
import renetik.android.R.styleable.CSLayout
import renetik.android.R.styleable.CSLayout_dispatchState


class CSTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), CSHasTouchEvent {

    override val self = this
    private val dispatchState: Boolean
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    init {
        clipToOutline = false
        val attributes = context.theme.obtainStyledAttributes(attrs, CSLayout, 0, 0)
        try {
            dispatchState = attributes.getBoolean(CSLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (minWidth != -1 && measuredWidth < minWidth) {
            super.onMeasure(makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec)
        } else if (maxWidth != -1 && measuredWidth > maxWidth) {
            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
        } else if (rotation == 270f || rotation == 90f)
            super.onMeasure(heightMeasureSpec, widthMeasureSpec)
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
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.onTouchEvent(event) else true
    }
}