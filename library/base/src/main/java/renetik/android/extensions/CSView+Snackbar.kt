package renetik.android.extensions

import renetik.android.extensions.view.snackBarError
import renetik.android.extensions.view.snackBarInfo
import renetik.android.extensions.view.snackBarWarn
import renetik.android.base.CSView

fun CSView<*>.snackBarWarn(text: String) = view.snackBarWarn(text)

fun CSView<*>.snackBarError(text: String) = view.snackBarError(text)

fun CSView<*>.snackBarInfo(text: String) = view.snackBarInfo(text)