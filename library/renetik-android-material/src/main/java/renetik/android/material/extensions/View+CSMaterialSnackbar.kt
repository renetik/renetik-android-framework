package renetik.android.material.extensions

import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.R.attr.colorError
import androidx.core.view.updateLayoutParams
import com.google.android.material.R.attr.colorOnError
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSurface
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.core.extensions.content.attributeColor
import renetik.android.core.lang.CSButtonAction
import renetik.android.core.lang.Func
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.util.CSLater.later
import renetik.android.ui.extensions.view.removeFromSuperview
import renetik.android.ui.extensions.widget.layoutMatch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun View.snackbarWarn(
    parent: CSHasRegistrations, title: String,
    time: Duration? = null, action: CSButtonAction? = null
) = snackbar(parent, title, backColor = context.attributeColor(colorError),
    textColor = context.attributeColor(colorOnError), action = action, time)

fun View.snackbarError(
    parent: CSHasRegistrations, title: String,
    time: Duration? = null, action: CSButtonAction? = null
) = snackbar(parent, title, backColor = context.attributeColor(colorError),
    textColor = context.attributeColor(colorOnError), action = action, time)

fun View.snackbarInfo(
    parent: CSHasRegistrations, title: String,
    time: Duration? = null, action: CSButtonAction? = null
) = snackbar(parent, title, backColor = context.attributeColor(colorSurface),
    textColor = context.attributeColor(colorPrimary), action = action, time)

fun View.snackbar(parent: CSHasRegistrations, title: String) =
    snackbar(parent, title, time = 5.seconds)

fun View.snackbar(
    parent: CSHasRegistrations,
    title: String,
    @ColorInt backColor: Int? = null,
    @ColorInt textColor: Int? = null,
    action: CSButtonAction? = null,
    time: Duration? = null
) {
    (this as? FrameLayout)?.also {
        FrameLayout(context).apply {
            id = android.R.id.content
            it.addView(this, layoutMatch)
            showBar(parent, title, backColor, textColor, time,
                action, onDismiss = { removeFromSuperview() }
            ).view.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = CENTER or BOTTOM; width = WRAP_CONTENT
            }
        }
    } ?: showBar(parent, title, backColor, textColor, time, action)
}

private fun View.showBar(
    parent: CSHasRegistrations,
    title: String, @ColorInt backColor: Int? = null, @ColorInt textColor: Int? = null,
    time: Duration?, action: CSButtonAction? = null, onDismiss: Func? = null
): Snackbar {
    val snackbar = make(this, title, LENGTH_INDEFINITE)
    action?.also { snackbar.setAction(action.title) { action.onClick(); snackbar.dismiss() } }
    val dismissTimer = if (action == null)
        parent.later(after = time ?: 5.seconds) { snackbar.dismiss() } else null
    snackbar.addCallback(object : Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            dismissTimer?.cancel(); onDismiss?.invoke()
        }
    })
    backColor?.let { snackbar.view.setBackgroundColor(it) }
    textColor?.let { snackbar.setTextColor(it) }
    textColor?.let { snackbar.setTextMaxLines(Int.MAX_VALUE) }
    textColor?.let { snackbar.setActionTextColor(it) }
    snackbar.show()
    return snackbar
}