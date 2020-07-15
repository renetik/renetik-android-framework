package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.Slider
import renetik.android.base.CSView
import renetik.android.view.extensions.findView

fun View.slider(id: Int) = findView<Slider>(id)!!

fun CSView<*>.slider(id: Int) = view.slider(id)

fun <T : Slider> T.onChange(listener: (T) -> Unit) = apply {
    addOnChangeListener { _, _, _ -> listener(this) }
}