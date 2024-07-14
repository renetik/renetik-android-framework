package renetik.android.material.controller

import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.base.CSView
import renetik.android.ui.extensions.findView
import renetik.android.ui.extensions.view.findView
import renetik.android.ui.extensions.view.onClick

fun CSView<*>.textInput(
    id: Int, onClick: ((TextInputLayout) -> Unit)? = null
) =
    view.findView<TextInputLayout>(id)!!.apply {
        onClick?.let { editText?.onClick { onClick(this) } }
    }

fun CSView<*>.switch(id: Int): SwitchCompat = findView(id)!!
fun CSView<*>.slider(id: Int): Slider = findView(id)!!
fun CSView<*>.rangeSlider(id: Int): RangeSlider = findView(id)!!