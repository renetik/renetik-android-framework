package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

open class CSHorizontalScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr, defStyleRes) {

    var isScrollEnabled = true
    var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.dispatchTouchEvent(event) else true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isScrollEnabled && super.onTouchEvent(ev)
    }

    var isInterceptTouch = true
    var isInterceptedTouch = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isInterceptTouch) {
            isInterceptedTouch = false
            return false
        }
        isInterceptedTouch = super.onInterceptTouchEvent(ev)
        return isInterceptedTouch
    }
}