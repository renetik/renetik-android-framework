package renetik.android.framework.view

import android.view.View
import androidx.annotation.IdRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.view.findView

fun View.swipeRefresh(@IdRes id: Int) = findView<SwipeRefreshLayout>(id)!!