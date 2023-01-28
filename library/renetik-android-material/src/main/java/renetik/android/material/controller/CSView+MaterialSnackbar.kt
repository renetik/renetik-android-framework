package renetik.android.material.controller

import renetik.android.controller.base.CSView
import renetik.android.controller.base.onClick
import renetik.android.core.lang.CSButtonAction
import renetik.android.material.extensions.snackBarError
import renetik.android.material.extensions.snackBarInfo
import renetik.android.material.extensions.snackBarWarn

fun CSView<*>.snackBarWarn(text: String, action: CSButtonAction? = null) =
    view.snackBarWarn(text, action)

fun CSView<*>.snackBarError(text: String, action: CSButtonAction? = null) =
    view.snackBarError(text, action)

fun CSView<*>.snackBarInfo(text: String, action: CSButtonAction? = null) =
    view.snackBarInfo(text, action)