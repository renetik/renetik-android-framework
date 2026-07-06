package renetik.android.controller.base

import android.view.View

fun View.asCSView() = asCS<CSView<*>>()

inline fun <reified CSViewType : CSView<*>> View.asCS() = this.tag as? CSViewType