package renetik.android.dialog.extensions

import android.app.Activity
import renetik.android.dialog.CSDialog

fun Activity.dialog() = CSDialog(this)