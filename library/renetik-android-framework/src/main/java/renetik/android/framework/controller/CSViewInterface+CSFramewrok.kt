package renetik.android.framework.controller

import androidx.annotation.IdRes
import renetik.android.framework.view.swipeRefresh
import renetik.android.ui.protocol.CSViewInterface

fun CSViewInterface.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)