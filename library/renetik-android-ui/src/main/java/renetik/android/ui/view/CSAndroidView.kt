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
    var minWidthParam: Int
    var maxWidthParam: Int
    var minHeightParam: Int
    var maxHeightParam: Int
    var dispatchStateParam: Boolean

    fun onReMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int>? {
        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec

        if (minWidthParam != -1 && self.measuredWidth < minWidthParam) {
            widthMeasure = makeMeasureSpec(minWidthParam, View.MeasureSpec.EXACTLY)
        } else if (maxWidthParam != -1 && self.measuredWidth > maxWidthParam) widthMeasure =
            makeMeasureSpec(maxWidthParam, View.MeasureSpec.EXACTLY)

        if (minHeightParam != -1 && self.measuredHeight < minHeightParam)
            heightMeasure = makeMeasureSpec(minHeightParam, View.MeasureSpec.EXACTLY)
        else if (maxHeightParam != -1 && self.measuredHeight > maxHeightParam)
            heightMeasure = makeMeasureSpec(maxHeightParam, View.MeasureSpec.EXACTLY)

        if (widthMeasure != widthMeasureSpec || heightMeasure != heightMeasureSpec)
            return widthMeasure to heightMeasure

        return null
    }
}

fun CSAndroidView.loadCSAttributes(attrs: AttributeSet?) {
    val attributes =
        self.context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
    try {
        self.clipToOutline = attributes.getBoolean(CSLayout_clipToOutline, self.clipToOutline)
        minWidthParam = attributes.getDimensionPixelSize(CSLayout_minWidth, -1)
        maxWidthParam = attributes.getDimensionPixelSize(CSLayout_maxWidth, -1)
        minHeightParam = attributes.getDimensionPixelSize(CSLayout_minHeight, -1)
        maxHeightParam = attributes.getDimensionPixelSize(CSLayout_maxHeight, -1)
        dispatchStateParam = attributes.getBoolean(CSLayout_dispatchState, true)
    } finally {
        attributes.recycle()
    }
}