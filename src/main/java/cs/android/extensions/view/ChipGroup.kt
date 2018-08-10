package cs.android.extensions.view

import com.google.android.material.chip.ChipGroup

fun <T : ChipGroup> T.onChange(onChange: (view: T) -> Unit): T {
    setOnCheckedChangeListener { _, _ -> onChange(this) }
    return this
}