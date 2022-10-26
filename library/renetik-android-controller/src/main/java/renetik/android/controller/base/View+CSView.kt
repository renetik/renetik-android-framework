package renetik.android.controller.base

import android.view.View

fun View.asCSView() = asCS<CSView<*>>()

@Suppress("UNCHECKED_CAST")
fun <CSViewType : CSView<*>> View.asCS() = this.tag as? CSViewType