package renetik.android.ui.extensions.widget

import android.widget.DatePicker
import renetik.android.core.java.util.calendar
import renetik.android.core.java.util.dateFrom
import java.util.Date

val DatePicker.date: Date get() = calendar.dateFrom(year, month, dayOfMonth)