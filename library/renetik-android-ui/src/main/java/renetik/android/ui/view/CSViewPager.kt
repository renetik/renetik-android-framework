package renetik.android.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import renetik.android.core.logging.CSLog.logWarn

class CSViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var isSwipePagingEnabled = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean = try {
        if (isSwipePagingEnabled) super.onTouchEvent(event) else false
    } catch (e: IllegalArgumentException) {
        if (e.isFrameworkBug()) logWarn(e) else throw e
        false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean = try {
        if (isSwipePagingEnabled) super.onInterceptTouchEvent(event) else false
    } catch (e: IllegalArgumentException) {
        if (e.isFrameworkBug()) logWarn(e) else throw e
        false
    }

    private fun IllegalArgumentException.isFrameworkBug() =
        this.message?.contains("pointerIndex", ignoreCase = true) == true
}