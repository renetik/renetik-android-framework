package renetik.android.material

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.Slider

class CSVerticalSlider @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    Slider(context, attrs, defStyleAttr) {
    init {
        rotation = 270f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
    }
}