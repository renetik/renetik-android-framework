package renetik.android.material.controller

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.base.CSView
import renetik.android.material.extensions.materialSwitch
import renetik.android.material.extensions.rangeSlider
import renetik.android.material.extensions.slider
import renetik.android.ui.extensions.findView
import renetik.android.ui.extensions.view.findView
import renetik.android.ui.extensions.view.onClick

fun CSView<*>.textInput(
    id: Int, onClick: ((TextInputLayout) -> Unit)? = null) =
    view.findView<TextInputLayout>(id)!!.apply {
        onClick?.let { editText?.onClick { onClick(this) } }
    }

fun CSView<*>.switch(id: Int): SwitchMaterial = view.materialSwitch(id)
fun CSView<*>.slider(id: Int): Slider = view.slider(id)
fun CSView<*>.rangeSlider(id: Int): RangeSlider = view.rangeSlider(id)
fun CSView<*>.chip(id: Int): Chip = findView<Chip>(id)!!
fun CSView<*>.chipGroup(id: Int): ChipGroup = findView<ChipGroup>(id)!!
fun CSView<*>.floatingButton(id: Int): FloatingActionButton = findView<FloatingActionButton>(id)!!