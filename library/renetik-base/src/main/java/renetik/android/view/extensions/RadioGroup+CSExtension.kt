package renetik.android.view.extensions

import android.widget.RadioGroup

fun RadioGroup.onCheckedChange(listener: (buttonId: Int) -> Unit) = apply {
    setOnCheckedChangeListener { _, checkedId -> listener(checkedId) }
}

val RadioGroup.checkedId get() = checkedRadioButtonId