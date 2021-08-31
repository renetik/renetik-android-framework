package renetik.android.controller.common

import androidx.annotation.AnimRes
import renetik.android.controller.R

enum class CSNavigationAnimation(@AnimRes val resource: Int) {
    FadeIn(R.anim.cs_fade_in),
    FadeOut(R.anim.cs_fade_out),
    SlideInTop(R.anim.abc_slide_in_top),
    SlideOutTop(R.anim.abc_slide_out_top),
    SlideInRight(R.anim.cs_slide_in_right),
    SlideOutLeft(R.anim.cs_slide_out_left),
    SlideInBottom(R.anim.abc_slide_in_bottom),
    SlideOutBottom(R.anim.abc_slide_out_bottom),
    None(0)
}