package renetik.android.material.extensions

import com.google.android.material.chip.ChipGroup

@Suppress("DEPRECATION")
fun <T : ChipGroup> T.onChange(onChange: (view: T) -> Unit): T {
    setOnCheckedChangeListener { _, _ -> onChange(this) }
    return this
}