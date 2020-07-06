package renetik.android.view.extensions

import android.widget.RadioGroup

fun RadioGroup.onCheckedChange(listener: (buttonId: Int) -> Unit) {
    setOnCheckedChangeListener { _, checkedId -> listener(checkedId) }
}