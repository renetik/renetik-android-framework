package renetik.android.controller.common

import androidx.annotation.AnimRes
import renetik.android.controller.R

enum class CSNavigationAnimation(@AnimRes val resource: Int) {
    FadeIn(renetik.android.ui.R.anim.cs_fade_in),
    FadeOut(renetik.android.ui.R.anim.cs_fade_out),
    SlideInTop(androidx.appcompat.R.anim.abc_slide_in_top),
    SlideOutTop(androidx.appcompat.R.anim.abc_slide_out_top),
    SlideInRight(renetik.android.ui.R.anim.cs_slide_in_right),
    SlideOutLeft(renetik.android.ui.R.anim.cs_slide_out_left),
    SlideInBottom(androidx.appcompat.R.anim.abc_slide_in_bottom),
    SlideOutBottom(androidx.appcompat.R.anim.abc_slide_out_bottom),
    None(0)
}