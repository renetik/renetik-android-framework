package renetik.android.material.controller

import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSAction
import renetik.android.event.common.onMain
import renetik.android.material.extensions.snackError
import renetik.android.material.extensions.snackInfo
import renetik.android.material.extensions.snackWarn
import kotlin.time.Duration

fun CSView<*>.snackWarn(
    text: String, time: Duration? = null,
    action: CSAction? = null
) = onMain { view.snackWarn(this, text, time, action) }

fun CSView<*>.snackError(
    text: String, title: String,
    time: Duration? = null, onClick: () -> Unit
) = onMain { view.snackError(this, text, time, CSAction(title, onClick)) }

fun CSView<*>.snackError(
    text: String, time: Duration? = null,
    action: CSAction? = null
) = onMain { view.snackError(this, text, time, action) }

fun CSView<*>.snackInfo(
    text: String, time: Duration? = null,
    action: CSAction? = null
) = onMain { view.snackInfo(this, text, time, action) }