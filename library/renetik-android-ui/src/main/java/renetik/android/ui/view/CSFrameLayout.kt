package renetik.android.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import kotlin.properties.Delegates.notNull

open class CSFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), CSHasTouchEvent, CSAndroidView {

    override val self: View get() = this
    override var minWidthParam: Int by notNull()
    override var maxWidthParam: Int by notNull()
    override var minHeightParam: Int by notNull()
    override var maxHeightParam: Int by notNull()
    override var dispatchStateParam: Boolean by notNull()

    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    var onDispatchTouchEvent: ((event: MotionEvent) -> Boolean)? = null
    val eventOnDraw = event<Canvas>()
    var eventOnLayout = event()

    init {
        loadCSAttributes(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMeasure = widthMeasureSpec
        if (minWidthParam != -1 && measuredWidth < minWidthParam)
            widthMeasure = makeMeasureSpec(minWidthParam, EXACTLY)
        else if (maxWidthParam != -1 && measuredWidth > maxWidthParam)
            widthMeasure = makeMeasureSpec(maxWidthParam, EXACTLY)

        var heightMeasure = heightMeasureSpec
        if (minHeightParam != -1 && measuredHeight < minHeightParam)
            heightMeasure = makeMeasureSpec(minHeightParam, EXACTLY)
        else if (maxHeightParam != -1 && measuredHeight > maxHeightParam)
            heightMeasure = makeMeasureSpec(maxHeightParam, EXACTLY)
        super.onMeasure(widthMeasure, heightMeasure)
    }

    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchStateParam) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchStateParam) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchStateParam) super.dispatchSetPressed(pressed)
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        eventOnLayout.fire()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        eventOnDraw.fire(canvas)
    }
}