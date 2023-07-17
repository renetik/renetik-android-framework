package renetik.android.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_UP
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import renetik.android.core.kotlin.equalsNone
import renetik.android.ui.R

class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr),
    CSHasTouchEvent {

    override val self = this
    private val _minWidth: Int
    private val _maxWidth: Int
    private val _maxHeight: Int
    var dispatchState: Boolean
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            clipToOutline = attributes.getBoolean(R.styleable.CSLayout_clipToOutline, false)
            _minWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_minWidth, -1)
            _maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth, -1)
            _maxHeight = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxHeight, -1)
            dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec

        if (_minWidth != -1 && measuredWidth < _minWidth) {
            widthMeasure = makeMeasureSpec(_minWidth, EXACTLY)
        } else if (_maxWidth != -1 && measuredWidth > _maxWidth)
            widthMeasure = makeMeasureSpec(_maxWidth, EXACTLY)

        if (_maxHeight != -1 && measuredHeight > _maxHeight)
            heightMeasure = makeMeasureSpec(_maxHeight, EXACTLY)
        super.onMeasure(widthMeasure, heightMeasure)
    }

    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchState) if (!isSelected) super.dispatchSetPressed(pressed)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO: make like this everywhere ?
        if (!isEnabled && event.actionMasked.equalsNone(ACTION_UP, ACTION_CANCEL)) return false
        val handled = onTouchEvent?.invoke(event) ?: false
        if (!isEnabled) return handled
        return if (!handled) super.onTouchEvent(event) else true
    }
}