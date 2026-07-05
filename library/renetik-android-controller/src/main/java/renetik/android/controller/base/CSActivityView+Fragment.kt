package renetik.android.controller.base

import android.view.View
import renetik.android.controller.base.CSActivityView

val <T : View>  CSActivityView<T>.fragmentTransaction
    get() = activity().supportFragmentManager.beginTransaction()