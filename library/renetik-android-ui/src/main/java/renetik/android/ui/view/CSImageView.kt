package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_UP
import androidx.appcompat.widget.AppCompatImageView
import renetik.android.core.kotlin.equalsNone
import kotlin.properties.Delegates.notNull

class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), CSHasTouchEvent, CSAndroidView {

    override val self = this
    override var csMinWidth: Int by notNull()
    override var csMaxWidth: Int by notNull()
    override var csMinHeight: Int by notNull()
    override var csMaxHeight: Int by notNull()
    override var csClipToOutline: Boolean by notNull()
    override var csDispatchState: Boolean by notNull()
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

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
        if (csDispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (csDispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (csDispatchState) if (!isSelected) super.dispatchSetPressed(pressed)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO: make like this everywhere ?
        if (!isEnabled && event.actionMasked.equalsNone(ACTION_UP, ACTION_CANCEL)) return false
        val handled = onTouchEvent?.invoke(event) ?: false
        if (!isEnabled) return handled
        return if (!handled) super.onTouchEvent(event) else true
    }
}