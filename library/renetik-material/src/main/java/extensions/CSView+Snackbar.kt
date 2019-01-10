package renetik.android.material.extensions

import renetik.android.base.CSView

fun CSView<*>.snackBarWarn(text: String) = view.snackBarWarn(text)
fun CSView<*>.snackBarError(text: String) = view.snackBarError(text)
fun CSView<*>.snackBarInfo(text: String) = view.snackBarInfo(text)