package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_UP
import androidx.appcompat.widget.AppCompatImageView
import renetik.android.core.kotlin.equalsNone
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import kotlin.properties.Delegates.notNull

class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), CSHasTouchEvent, CSAndroidView {

    override val self = this
    override var minWidthParam: Int by notNull()
    override var maxWidthParam: Int by notNull()
    override var minHeightParam: Int by notNull()
    override var maxHeightParam: Int by notNull()
    override var dispatchStateParam: Boolean by notNull()
    override val eventOnTouch: CSEvent<CSTouchEventArgs> = event<CSTouchEventArgs>()

    init {
        loadCSAttributes(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onReMeasure(widthMeasureSpec, heightMeasureSpec)?.let {
            super.onMeasure(it.first, it.second)
        }
    }


    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchStateParam) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchStateParam) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchStateParam) if (!isSelected) super.dispatchSetPressed(pressed)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO: make like this everywhere ?
        if (!isEnabled && event.actionMasked.equalsNone(ACTION_UP, ACTION_CANCEL)) return false
        val handled = processTouchEvent(event)
        if (!isEnabled) return handled
        return if (!handled) super.onTouchEvent(event) else true
    }
}