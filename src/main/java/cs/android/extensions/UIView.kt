package cs.android.extensions

import android.view.View
import cs.android.viewbase.CSView

fun CSView<View>.visible(): Boolean = isVisible(asView())

fun CSView<View>.shown(): Boolean = isShown(asView())
