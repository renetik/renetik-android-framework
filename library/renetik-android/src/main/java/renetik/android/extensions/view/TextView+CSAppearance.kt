package renetik.android.extensions.view

import android.widget.TextView
import org.jetbrains.anko.textColor
import renetik.android.extensions.color
import renetik.android.model.application

fun TextView.textColor(value: Int) = apply { textColor = application.color(value) }