package renetik.android.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import renetik.android.core.logging.CSLog.logWarn

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = try {
        isScrollEnabled && super.onTouchEvent(ev)
    } catch (e: IllegalArgumentException) {
        if (e.isFrameworkBug()) logWarn(e) else throw e
        false
    }

    var isInterceptTouch = true
    var isInterceptedTouch = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isInterceptTouch) {
            isInterceptedTouch = false
            return false
        }
        try {
            isInterceptedTouch = super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            if (e.isFrameworkBug()) logWarn(e) else throw e
        }
        return isInterceptedTouch
    }

    private fun IllegalArgumentException.isFrameworkBug() =
        this.message?.contains("pointerIndex", ignoreCase = true) == true
}