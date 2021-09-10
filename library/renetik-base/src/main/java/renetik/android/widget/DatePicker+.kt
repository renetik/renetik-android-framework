package renetik.android.widget

import android.widget.DatePicker
import renetik.java.util.calendar
import renetik.java.util.dateFrom
import java.util.*

val DatePicker.date: Date get() = calendar.dateFrom(year, month, dayOfMonth)