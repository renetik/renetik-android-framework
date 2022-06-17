package renetik.android.framework.view

import android.view.View
import androidx.annotation.IdRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.extensions.findView

fun View.swipeRefresh(@IdRes id: Int) = findView<SwipeRefreshLayout>(id)!!