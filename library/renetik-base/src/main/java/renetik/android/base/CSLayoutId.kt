package renetik.android.base

import androidx.annotation.LayoutRes

fun layout(@LayoutRes id: Int) = CSLayoutId(id)

class CSLayoutId(@LayoutRes val id: Int)