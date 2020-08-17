package renetik.android.view.extensions

import android.view.View
import android.widget.RadioGroup
import androidx.annotation.IdRes
import renetik.android.base.CSView

fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!

fun CSView<*>.radioGroup(@IdRes id: Int, onChanged: ((buttonId: Int) -> Unit)? = null): RadioGroup =
    view.radioGroup(id).apply { onChanged?.let { this.onChanged(it) } }


fun RadioGroup.onChanged(listener: (buttonId: Int) -> Unit) = apply {
    setOnCheckedChangeListener { _, checkedId -> listener(checkedId) }
}

val RadioGroup.checkedId get() = checkedRadioButtonId
val RadioGroup.selectedId get() = checkedRadioButtonId