package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.ImageView
import renetik.android.R

interface CSHasTouchEvent {
    val self: View
    var onTouchEvent: ((event: MotionEvent) -> Boolean)?
}

fun CSHasTouchEvent.onTouch(function: (Boolean) -> Unit) {
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_DOWN -> true.also {
                self.isPressed = true
                function(true)
            }
            ACTION_UP, ACTION_CANCEL -> true.also {
                self.isPressed = false
                function(false)
            }
            ACTION_MOVE -> true
            else -> false
        }
    }
}

fun <T : CSHasTouchEvent> T.setTogglePressed(pressed: Boolean) = apply {
    if (pressed) {
        self.isSelected = true
        self.isActivated = true
        self.isPressed = false
    } else {
        self.isSelected = false
        self.isActivated = false
        self.isPressed = false
    }
}

fun <T : CSHasTouchEvent> T.onTouchToggle(function: (Boolean) -> Unit) = apply {
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_DOWN -> true.also {
                if (!self.isSelected) {
                    function(true)
                    self.isActivated = true
                } else self.isActivated = false
                self.isPressed = true
            }
            ACTION_UP, ACTION_CANCEL -> true.also {
                if (self.isActivated) setTogglePressed(true)
                else {
                    function(false)
                    setTogglePressed(false)
                }
            }
            ACTION_MOVE -> true
            else -> false
        }
    }
}

class CSImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes), CSHasTouchEvent {

    override val self = this
    private val _maxHeight: Int
    override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            clipToOutline = attributes.getBoolean(R.styleable.CSLayout_clipToOutline, false)
            _maxHeight = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxHeight, -1)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (_maxHeight != -1 && measuredHeight > _maxHeight)
            super.onMeasure(widthMeasureSpec, makeMeasureSpec(_maxHeight, EXACTLY))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = onTouchEvent?.invoke(event) ?: false
        return if (!handled) super.onTouchEvent(event) else true
    }
}