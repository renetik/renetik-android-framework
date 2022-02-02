package renetik.java.lang

import android.os.Looper

val Thread.isMain get() = Looper.getMainLooper().thread == this
