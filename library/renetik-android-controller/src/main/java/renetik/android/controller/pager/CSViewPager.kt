package renetik.android.controller.pager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

class CSViewPager(context: Context, attrs: AttributeSet) :
    ViewPager(context, attrs) {

    var isSwipePagingEnabled = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean =
        if (isSwipePagingEnabled) super.onTouchEvent(event) else false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean =
        if (isSwipePagingEnabled) super.onInterceptTouchEvent(event) else false

}