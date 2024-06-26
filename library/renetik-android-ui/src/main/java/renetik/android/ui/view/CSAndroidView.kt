package renetik.android.ui.view

import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.makeMeasureSpec
import renetik.android.ui.R
import renetik.android.ui.R.styleable.CSLayout_clipToOutline
import renetik.android.ui.R.styleable.CSLayout_dispatchState
import renetik.android.ui.R.styleable.CSLayout_maxHeight
import renetik.android.ui.R.styleable.CSLayout_maxWidth
import renetik.android.ui.R.styleable.CSLayout_minHeight
import renetik.android.ui.R.styleable.CSLayout_minWidth

interface CSAndroidView {

    val self: View
    var csMinWidth: Int
    var csMaxWidth: Int
    var csMinHeight: Int
    var csMaxHeight: Int
    var csClipToOutline: Boolean
    var csDispatchState: Boolean

    fun loadCSAttributes(attrs: AttributeSet?) {
        val attributes =
            self.context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
            csClipToOutline = attributes.getBoolean(CSLayout_clipToOutline, false)
            csMinWidth = attributes.getDimensionPixelSize(CSLayout_minWidth, -1)
            csMaxWidth = attributes.getDimensionPixelSize(CSLayout_maxWidth, -1)
            csMinHeight = attributes.getDimensionPixelSize(CSLayout_minHeight, -1)
            csMaxHeight = attributes.getDimensionPixelSize(CSLayout_maxHeight, -1)
            csDispatchState = attributes.getBoolean(CSLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    fun onReMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int>? {
        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec

        if (csMinWidth != -1 && self.measuredWidth < csMinWidth) {
            widthMeasure = makeMeasureSpec(csMinWidth, View.MeasureSpec.EXACTLY)
        } else if (csMaxWidth != -1 && self.measuredWidth > csMaxWidth) widthMeasure =
            makeMeasureSpec(csMaxWidth, View.MeasureSpec.EXACTLY)

        if (csMinHeight != -1 && self.measuredHeight < csMinHeight)
            heightMeasure = makeMeasureSpec(csMinHeight, View.MeasureSpec.EXACTLY)
        else if (csMaxHeight != -1 && self.measuredHeight > csMaxHeight)
            heightMeasure = makeMeasureSpec(csMaxHeight, View.MeasureSpec.EXACTLY)

        if (widthMeasure != widthMeasureSpec || heightMeasure != heightMeasureSpec)
            return widthMeasure to heightMeasure

        return null
    }
}