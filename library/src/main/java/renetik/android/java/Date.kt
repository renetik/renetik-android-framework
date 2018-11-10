package renetik.android.java

import java.text.DateFormat
import java.util.*

fun Date.format(dateStyle: Int, timeStyle: Int): String = DateFormat.getDateTimeInstance(dateStyle, timeStyle).format(this)
fun Date.formatDate(style: Int): String = DateFormat.getDateInstance(style).format(this)
fun Date.formatTime(style: Int): String = DateFormat.getTimeInstance(style).format(this)