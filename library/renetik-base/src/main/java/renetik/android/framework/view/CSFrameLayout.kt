package renetik.android.framework.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import renetik.android.R
import renetik.android.framework.event.event


class CSFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val minWidth: Int
    private val maxWidth: Int
    private val dispatchState: Boolean
    val eventOnDraw = event<Canvas>()
//    var onDrawEvent: ((canvas: Canvas) -> Unit)? = null
    var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            minWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_minWidth, -1)
            maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth, -1)
            dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
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
        }
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        eventOnDraw.fire(canvas)
//        onDrawEvent?.invoke(canvas)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = super.dispatchTouchEvent(event)
        if (!handled) onTouchEvent?.let { return it.invoke(event) }
        return handled
    }
}