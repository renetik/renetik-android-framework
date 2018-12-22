package renetik.android.material.extensions

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import renetik.android.base.CSView
import renetik.android.extensions.findView

fun CSView<*>.chip(id: Int) = findView<Chip>(id)
fun CSView<*>.chipGroup(id: Int): ChipGroup = findView(id)
fun CSView<*>.floatingButton(id: Int): FloatingActionButton = findView(id)
