package renetik.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.LinearLayout
import renetik.android.R


class CSLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val maxWidth: Int
    private val dispatchState: Boolean

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLinearLayout, 0, 0)
        try {
            maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLinearLayout_maxWidth, -1)
            dispatchState = attributes.getBoolean(R.styleable.CSLinearLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (maxWidth != -1 && measuredWidth > maxWidth) {
            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
        }
    }

    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchState) super.dispatchSetPressed(pressed)
    }
}