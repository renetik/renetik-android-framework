package renetik.android.view.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.onRefresh(function: (SwipeRefreshLayout) -> Unit) = setOnRefreshListener { function(this) }

fun SwipeRefreshLayout.onDone() = apply { isRefreshing = false }