package renetik.android.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.LinearLayout
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.ui.R

open class CSLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), CSHasTouchEvent {

    val minWidth: Int?
    val maxWidth: Int?
    var dispatchState: Boolean
    var onDispatchTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    override val self: View get() = this
    override val eventOnTouch: CSEvent<CSTouchEventArgs> = event<CSTouchEventArgs>()
    val eventOnDraw = event<Canvas>()
    var eventOnLayout = event()

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        clipToOutline = attributes.getBoolean(R.styleable.CSLayout_clipToOutline, false)
        minWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_minWidth)
        maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth)
        dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (minWidth != null && measuredWidth < minWidth) super.onMeasure(
            makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec
        )
        else if (maxWidth != null && measuredWidth > maxWidth) super.onMeasure(
            makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec
        )
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
        return if (processTouchEvent(event)) true else super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        eventOnDraw.fire(canvas)
    }

    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int, bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        eventOnLayout.fire()
    }
}