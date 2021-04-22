package renetik.android.widget

import android.widget.TimePicker
import renetik.android.java.util.calendar
import renetik.android.java.util.timeFrom

@Suppress("DEPRECATION") val TimePicker.time
    get() = calendar.timeFrom(currentHour, currentMinute)