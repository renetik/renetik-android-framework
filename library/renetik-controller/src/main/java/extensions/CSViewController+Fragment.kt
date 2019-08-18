package renetik.android.controller.extensions

import android.view.View
import renetik.android.controller.base.CSViewController

val <T : View>  CSViewController<T>.fragmentTransaction
    get() = activity().supportFragmentManager.beginTransaction()