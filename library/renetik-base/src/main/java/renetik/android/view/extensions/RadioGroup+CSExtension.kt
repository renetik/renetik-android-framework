package renetik.android.view.extensions

import android.view.View
import android.widget.RadioGroup
import androidx.annotation.IdRes
import renetik.android.R
import renetik.android.java.event.event
import renetik.android.java.event.listen

fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!

val <T : RadioGroup> T.eventChange
    get() = propertyWithTag(R.id.RadioGroupEventOnChangeTagKey) { event<Int>() }

fun RadioGroup.onChange(listener: (buttonId: Int) -> Unit) = apply {
    eventChange.listen(listener)
    setOnCheckedChangeListener { _, checkedId -> eventChange.fire(checkedId) }
}

val RadioGroup.checkedId get() = checkedRadioButtonId

val RadioGroup.isChecked get() = checkedRadioButtonId != -1