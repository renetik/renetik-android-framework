package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.widget.HorizontalScrollView

open class CSHorizontalScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr, defStyleRes) {

    var isScrollEnabled = true
    var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

//    init {
//        setOnTouchListener { _, _ -> !isScrollEnabled }
//    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.dispatchTouchEvent(event) else true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isScrollEnabled && super.onTouchEvent(ev)
    }

//    override fun onTouchEvent(ev: MotionEvent) = when (ev.action) {
//        ACTION_DOWN -> isScrollEnabled && super.onTouchEvent(ev)
//        else -> super.onTouchEvent(ev)
//    }

//    override fun onInterceptTouchEvent(ev: MotionEvent) =
//        isScrollEnabled && super.onInterceptTouchEvent(ev)
}