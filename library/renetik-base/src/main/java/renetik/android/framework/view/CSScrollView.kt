package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView


class CSScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ScrollView(context, attrs, defStyleAttr, defStyleRes) {

    var isScrollEnabled = true
    var onDispatchTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = onDispatchTouchEvent?.invoke(event) ?: false
        return if (!handled) super.dispatchTouchEvent(event) else true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isScrollEnabled && super.onTouchEvent(ev)
    }
}