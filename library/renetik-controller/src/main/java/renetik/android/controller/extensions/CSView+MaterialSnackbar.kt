package renetik.android.controller.extensions

import renetik.android.framework.CSView
import renetik.android.material.extensions.snackBarError
import renetik.android.material.extensions.snackBarInfo
import renetik.android.material.extensions.snackBarWarn

fun CSView<*>.snackBarWarn(text: String) = view.snackBarWarn(text)
fun CSView<*>.snackBarError(text: String) = view.snackBarError(text)
fun CSView<*>.snackBarInfo(text: String) = view.snackBarInfo(text)