package renetik.android.view.extensions

import android.view.View

val <T : View> T.topFromBottom get() = superview?.let { it.height - top } ?: height

