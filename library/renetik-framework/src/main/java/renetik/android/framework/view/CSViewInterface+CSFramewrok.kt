package renetik.android.framework.view

import androidx.annotation.IdRes
import renetik.android.framework.protocol.CSViewInterface

fun CSViewInterface.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)