package renetik.android.material

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial

class CSVerticalSlider @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Slider(context, attrs, defStyleAttr) {
    init {
        rotation = 270f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
    }
}

class CSVerticalSwitch @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SwitchMaterial(context, attrs, defStyleAttr) {

    init {
        rotation = 90f
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
//    }
}