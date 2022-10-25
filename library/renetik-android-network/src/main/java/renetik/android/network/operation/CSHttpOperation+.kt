package renetik.android.network.operation

fun <Data : Any> CSHttpOperation<Data>.refresh() = apply { isRefresh = true }