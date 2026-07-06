package renetik.android.controller.navigation

import android.annotation.SuppressLint
import androidx.annotation.AnimRes
import androidx.appcompat.R.anim.abc_slide_in_bottom
import androidx.appcompat.R.anim.abc_slide_in_top
import androidx.appcompat.R.anim.abc_slide_out_bottom
import androidx.appcompat.R.anim.abc_slide_out_top
import renetik.android.ui.R.anim.cs_fade_in
import renetik.android.ui.R.anim.cs_fade_out
import renetik.android.ui.R.anim.cs_slide_in_right
import renetik.android.ui.R.anim.cs_slide_out_left

@SuppressLint("PrivateResource")
enum class CSNavigationAnimation(@AnimRes val resource: Int) {
    FadeIn(cs_fade_in),
    FadeOut(cs_fade_out),
    SlideInTop(abc_slide_in_top),
    SlideOutTop(abc_slide_out_top),
    SlideInRight(cs_slide_in_right),
    SlideOutLeft(cs_slide_out_left),
    SlideInBottom(abc_slide_in_bottom),
    SlideOutBottom(abc_slide_out_bottom),
    None(0)
}