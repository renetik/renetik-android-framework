package renetik.android.view.extensions

import android.view.View
import android.widget.RadioGroup
import androidx.annotation.IdRes
import renetik.android.R
import renetik.android.base.CSView
import renetik.android.java.event.event
import renetik.android.java.event.listener

fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!

fun CSView<*>.radioGroup(@IdRes id: Int, onChanged: ((buttonId: Int) -> Unit)? = null): RadioGroup =
    view.radioGroup(id).apply { onChanged?.let { this.onChange(it) } }

val <T : RadioGroup> T.eventChange
    get() = propertyWithTag(R.id.RadioGroupEventOnChangeTagKey) { event<Int>() }

fun RadioGroup.onChange(listener: (buttonId: Int) -> Unit) = apply {
    eventChange.listener(listener)
    setOnCheckedChangeListener { _, checkedId -> eventChange.fire(checkedId) }
}

val RadioGroup.checkedId get() = checkedRadioButtonId

val RadioGroup.selectedId get() = checkedRadioButtonId