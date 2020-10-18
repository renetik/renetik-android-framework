package renetik.android.material.extensions

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.extensions.colorFromAttribute
import renetik.android.material.R

private fun View.snackBar(text: String) = snackBar(text, Snackbar.LENGTH_SHORT)

private fun View.snackBar(text: String, time: Int) = make(this, text, time).show()

private fun View.snackBar(text: String, backColor: Int, textColor: Int) =
    snackBar(text, backColor, textColor, Snackbar.LENGTH_SHORT)

private fun View.snackBar(text: String, backColor: Int, textColor: Int, time: Int) =
    make(this, text, time).apply {
        view.setBackgroundColor(backColor)
        (view.findViewById(R.id.snackbar_text) as? TextView)?.setTextColor(textColor)
    }.show()

fun View.snackBarWarn(text: String) =
    snackBar(text,
        context.colorFromAttribute(R.attr.colorError),
        context.colorFromAttribute(R.attr.colorOnError))

fun View.snackBarError(text: String) =
    snackBar(text,
        context.colorFromAttribute(R.attr.colorError),
        context.colorFromAttribute(R.attr.colorOnError))

fun View.snackBarInfo(text: String)=
    snackBar(text,
        context.colorFromAttribute(R.attr.colorPrimary),
        context.colorFromAttribute(R.attr.colorOnPrimary))