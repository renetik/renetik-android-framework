package renetik.android.controller.extensions

import android.view.View
import renetik.android.controller.base.CSActivityView

val <T : View>  CSActivityView<T>.fragmentTransaction
    get() = activity().supportFragmentManager.beginTransaction()