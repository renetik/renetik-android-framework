package renetik.android.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.withSave
import renetik.android.ui.R
import renetik.android.ui.R.styleable.CSLayout_isRotatedClockwise
import kotlin.math.abs

class CSVerticalTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private val textBounds = Rect()
    private var minTextSize = 12
    private var maxTextSize = 112

    var isRotatedClockwise: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var isAutoSized: Boolean = false //TODO when necessary
        set(value) {
            field = value
            if (value) requestLayout()
        }

    init {
        includeFontPadding = false
        context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0).let {
            isRotatedClockwise = it.getBoolean(CSLayout_isRotatedClockwise, isRotatedClockwise)
            it.recycle()
        }
        isAutoSized = autoSizeTextType == 1
        minTextSize = autoSizeMinTextSize
        maxTextSize = autoSizeMaxTextSize
        super.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_NONE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        if (isAutoSized && parentHeight > 0 && parentWidth > 0 && text.isNotEmpty()) {
            adjustTextSize(parentHeight, parentWidth)
        }
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    private fun adjustTextSize(parentHeight: Int, parentWidth: Int) {
        val targetTextWidth =
            parentHeight.toFloat() - (paddingTop + paddingBottom) // Was paddingLeft/Right, vertical uses Top/Bottom in view coords
        val targetTextHeight = parentWidth.toFloat() - (paddingLeft + paddingRight)
        val testPaint = paint
        testPaint.textSize = 100f
        testPaint.getTextBounds(text.toString(), 0, text.length, textBounds)
        val currentVisualHeight = textBounds.height().toFloat()
        val currentVisualWidth = textBounds.width().toFloat()
        if (currentVisualHeight > 0 && currentVisualWidth > 0) {
            val scaleX = targetTextWidth / currentVisualWidth
            val scaleY = targetTextHeight / currentVisualHeight
            var newSize = 100f * minOf(scaleX, scaleY) * 0.9f
            newSize = newSize.coerceIn(minTextSize.toFloat(), maxTextSize.toFloat())
            if (abs(textSize - newSize) > 1f) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    override fun onDraw(canvas: Canvas) {
        val textLayout = layout ?: return
        canvas.withSave {
            if (isRotatedClockwise) {
                translate(width.toFloat(), 0f)
                rotate(90f)
            } else {
                translate(0f, height.toFloat())
                rotate(-90f)
            }

            val viewThickness = width.toFloat()
            val layoutThickness = textLayout.height.toFloat()
            var yOffset: Float
            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
            val availableThickness = viewThickness - paddingTop - paddingBottom
            yOffset = when (verticalGravity) {
                Gravity.CENTER_VERTICAL -> paddingTop + (availableThickness - layoutThickness) / 2f
                Gravity.BOTTOM -> viewThickness - paddingBottom - layoutThickness
                else -> paddingTop.toFloat()
            }

            val viewLength = height.toFloat()
            val layoutLength = textLayout.width.toFloat()
            var xOffset: Float
            val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
            val availableLength = viewLength - paddingLeft - paddingRight
            xOffset = when (horizontalGravity) {
                Gravity.CENTER_HORIZONTAL -> paddingLeft + (availableLength - layoutLength) / 2f
                Gravity.RIGHT -> viewLength - paddingRight - layoutLength
                else -> paddingLeft.toFloat()
            }

            translate(xOffset, yOffset)
            paint.color = currentTextColor
            paint.drawableState = drawableState
            textLayout.draw(this)
        }
    }
}