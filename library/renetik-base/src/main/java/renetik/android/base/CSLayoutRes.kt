package renetik.android.base

import androidx.annotation.LayoutRes

fun layout(@LayoutRes id: Int) = CSLayoutRes(id)

class CSLayoutRes(@LayoutRes val id: Int)