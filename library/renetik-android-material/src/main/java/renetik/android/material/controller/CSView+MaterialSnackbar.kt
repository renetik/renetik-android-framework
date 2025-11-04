package renetik.android.material.controller

import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSButtonAction
import renetik.android.event.util.CSLater.onMain
import renetik.android.material.extensions.snackbarError
import renetik.android.material.extensions.snackbarInfo
import renetik.android.material.extensions.snackbarWarn
import kotlin.time.Duration

fun CSView<*>.snackBarWarn(
    text: String, time: Duration? = null,
    action: CSButtonAction? = null
) = onMain { view.snackbarWarn(this, text, time, action) }

fun CSView<*>.snackBarError(
    text: String, title: String,
    time: Duration? = null, onClick: () -> Unit
) = onMain { view.snackbarError(this, text, time, CSButtonAction(title, onClick)) }

fun CSView<*>.snackBarError(
    text: String, time: Duration? = null,
    action: CSButtonAction? = null
) = onMain { view.snackbarError(this, text, time, action) }

fun CSView<*>.snackBarInfo(
    text: String, time: Duration? = null,
    action: CSButtonAction? = null
) = onMain { view.snackbarInfo(this, text, time, action) }