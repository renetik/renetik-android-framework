package renetik.android.view.extensions

import android.widget.RadioGroup

fun RadioGroup.onChanged(listener: (buttonId: Int) -> Unit) = apply {
    setOnCheckedChangeListener { _, checkedId -> listener(checkedId) }
}

val RadioGroup.checkedId get() = checkedRadioButtonId