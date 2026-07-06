package renetik.android.material

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.Slider

class CSSlider @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    Slider(context, attrs, defStyleAttr), CSMaterialSlider