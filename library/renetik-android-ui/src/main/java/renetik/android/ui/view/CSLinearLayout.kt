package renetik.android.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.LinearLayout
import renetik.android.ui.R
import renetik.android.core.kotlin.primitives.Empty
import renetik.android.core.kotlin.primitives.isSet

open class CSLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), CSHasTouchEvent {

    var minWidth: Int
    var maxWidth: Int
    var dispatchState: Boolean
    var onDispatchTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    override val self: View get() = this
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    var onDraw: ((event: Canvas) -> Unit)? = null

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            clipToOutline = attributes.getBoolean(R.styleable.CSLayout_clipToOutline, false)
            minWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_minWidth, Int.Empty)
            maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth, Int.Empty)
            dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (minWidth.isSet && measuredWidth < minWidth)
            super.onMeasure(makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec)
        else if (maxWidth.isSet && measuredWidth > maxWidth)
            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
    }

    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchState) if (!isSelected) super.dispatchSetPressed(pressed)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false
        val handled = onDispatchTouchEvent?.invoke(event) ?: false
        return if (!handled) super.dispatchTouchEvent(event) else true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.onTouchEvent(event) else true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDraw?.invoke(canvas)
    }
}