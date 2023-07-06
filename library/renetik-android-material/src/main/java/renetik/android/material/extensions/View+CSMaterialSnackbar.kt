package renetik.android.material.extensions

import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.R.attr.colorError
import com.google.android.material.R.attr.colorOnError
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSurface
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.core.extensions.content.attributeColor
import renetik.android.core.kotlin.notNull
import renetik.android.core.lang.CSButtonAction

fun View.snackBar(title: String) = snackBar(title, time = LENGTH_LONG)

fun View.snackBar(
    title: String, @ColorInt backColor: Int? = null, @ColorInt textColor: Int? = null,
    time: Int = LENGTH_LONG, action: CSButtonAction? = null
): Snackbar {
    val bar = make(this, title, if (action.notNull) LENGTH_INDEFINITE else time)
    action?.let {
        bar.setAction(action.title) {
            action.onClick()
            bar.dismiss()
        }
    }
    backColor?.let { bar.view.setBackgroundColor(it) }
    textColor?.let { bar.setTextColor(it) }
    textColor?.let { bar.setTextMaxLines(Int.MAX_VALUE) }
    textColor?.let { bar.setActionTextColor(it) }
    bar.show()
    return bar
}

fun View.snackBarWarn(title: String, action: CSButtonAction? = null) =
    snackBar(
        title, backColor = context.attributeColor(colorError),
        textColor = context.attributeColor(colorOnError), action = action
    )

fun View.snackBarError(title: String, action: CSButtonAction? = null) =
    snackBar(
        title, backColor = context.attributeColor(colorError),
        textColor = context.attributeColor(colorOnError), action = action
    )

fun View.snackBarInfo(title: String, action: CSButtonAction? = null) =
    snackBar(
        title, backColor = context.attributeColor(colorSurface),
        textColor = context.attributeColor(colorPrimary), action = action
    )