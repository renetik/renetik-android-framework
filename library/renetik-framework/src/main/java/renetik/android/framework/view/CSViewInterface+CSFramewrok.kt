package renetik.android.framework.view

import androidx.annotation.IdRes
import renetik.android.ui.protocol.CSViewInterface

fun CSViewInterface.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)