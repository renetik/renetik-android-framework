package renetik.android.extensions.view

import android.view.View
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.R
import renetik.android.model.application
import renetik.android.extensions.resourceColor
import renetik.java.lang.CSLang

private fun View.snackBar(text: String) {
    snackBar(text, 5 * CSLang.SECOND)
}

private fun View.snackBar(text: String, time: Int) {
    make(this, text, time).show()
}

private fun View.snackBar(text: String, backColor: Int, textColor: Int) {
    snackBar(text, backColor, textColor, 5 * CSLang.SECOND)
}

private fun View.snackBar(text: String, backColor: Int, textColor: Int, time: Int) {
    make(this, text, time).apply {
        view.setBackgroundColor(backColor)
//        (view.findViewById(R.id.snackbar_text) as? TextView)?.setTextColor(textColor)
        //TODO commented becouse unresolved id dont know why
    }.show()
}

fun View.snackBarWarn(text: String) {
    snackBar(text, application.resourceColor(R.color.cs_yellow), application.resourceColor(R.color.cs_black))
}

fun View.snackBarError(text: String) {
    snackBar(text, application.resourceColor(R.color.cs_red), application.resourceColor(R.color.cs_white))
}

fun View.snackBarInfo(text: String) {
    snackBar(text, application.resourceColor(R.color.cs_blue), application.resourceColor(R.color.cs_white))
}