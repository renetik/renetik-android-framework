package renetik.android.java.extensions

import android.os.Looper

val Thread.isMain get() = Looper.getMainLooper().thread == Thread.currentThread()
