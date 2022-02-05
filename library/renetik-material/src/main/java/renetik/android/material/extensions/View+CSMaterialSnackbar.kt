package renetik.android.material.extensions

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.content.attributeColor
import renetik.android.framework.lang.CSUserAction
import renetik.android.material.R

private fun View.snackBar(text: String) = snackBar(text, LENGTH_LONG)

private fun View.snackBar(text: String, time: Int) = make(this, text, time).show()

private fun View.snackBar(
    text: String, backColor: Int, textColor: Int, action: CSUserAction? = null) =
    snackBar(text, backColor, textColor, LENGTH_LONG, action)

private fun View.snackBar(
    text: String, backColor: Int, textColor: Int, time: Int, action: CSUserAction? = null) =
    make(this, text, if (action == null) time else LENGTH_INDEFINITE).apply {
        action?.let {
            this.setAction(action.title) {
                action.onClick()
                this.dismiss()
            }
        }
        view.setBackgroundColor(backColor)
        (view.findViewById(com.google.android.material.R.id.snackbar_text) as? TextView)?.setTextColor(textColor)
    }.show()

fun View.snackBarWarn(text: String, action: CSUserAction? = null) =
    snackBar(text, context.attributeColor(androidx.appcompat.R.attr.colorError),
        context.attributeColor(com.google.android.material.R.attr.colorOnError), action)

fun View.snackBarError(text: String, action: CSUserAction? = null) =
    snackBar(text, context.attributeColor(androidx.appcompat.R.attr.colorError),
        context.attributeColor(com.google.android.material.R.attr.colorOnError), action)

fun View.snackBarInfo(text: String, action: CSUserAction? = null) =
    snackBar(text, context.attributeColor(androidx.appcompat.R.attr.colorPrimary),
        context.attributeColor(com.google.android.material.R.attr.colorOnPrimary), action)