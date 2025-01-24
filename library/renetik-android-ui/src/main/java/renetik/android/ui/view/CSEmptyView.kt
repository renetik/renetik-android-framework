package renetik.android.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import renetik.android.core.extensions.graphics.height
import renetik.android.core.extensions.graphics.width
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.ui.R.styleable.CSLayout
import renetik.android.ui.R.styleable.CSLayout_dispatchState
import renetik.android.ui.R.styleable.CSLayout_goneIfHeightUntil
import renetik.android.ui.R.styleable.CSLayout_goneIfWidthUntil
import renetik.android.ui.R.styleable.CSLayout_maxWidth
import renetik.android.ui.R.styleable.CSLayout_minWidth
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.windowRectangle

open class CSEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes),
    CSHasTouchEvent, CSHasDrawEvent {

    override val self: View get() = this
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    private val minWidth: Int
    private val maxWidth: Int
    private val goneIfWidthUntil: Int
    private val goneIfHeightUntil: Int

    var dispatchState: Boolean
    var onDispatchTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    val eventOnDraw: CSEvent<Canvas> = event<Canvas>()
    override fun listenOnDraw(listener: (Canvas) -> Unit) = eventOnDraw.listen(listener)
    val eventOnLayout = event()

    init {
        context.theme.obtainStyledAttributes(attrs, CSLayout, 0, 0).let {
            minWidth = it.getDimensionPixelSize(CSLayout_minWidth, -1)
            maxWidth = it.getDimensionPixelSize(CSLayout_maxWidth, -1)
            dispatchState = it.getBoolean(CSLayout_dispatchState, true)
            goneIfWidthUntil = it.getDimensionPixelSize(CSLayout_goneIfWidthUntil, -1)
            goneIfHeightUntil = it.getDimensionPixelSize(CSLayout_goneIfHeightUntil, -1)
            it.recycle()
        }
        if (goneIfWidthUntil != -1)
            gone(windowRectangle.width <= goneIfWidthUntil)
        else if (goneIfHeightUntil != -1)
            gone(windowRectangle.height <= goneIfHeightUntil)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (minWidth != -1 && measuredWidth < minWidth) super.onMeasure(
            makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec
        )
        else if (maxWidth != -1 && measuredWidth > maxWidth) super.onMeasure(
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
        if (dispatchState) super.dispatchSetPressed(pressed)
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

    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int, bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
         if (changed) eventOnLayout.fire()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        eventOnDraw.fire(canvas)
    }
}