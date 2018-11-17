package renetik.android.extensions

import android.widget.TextView
import com.google.android.material.R.id
import com.google.android.material.snackbar.Snackbar.make
import renetik.android.R
import renetik.android.viewbase.CSView
import renetik.android.lang.CSLang.SECOND

private fun CSView<*>.snackBar(text: String) {
    snackBar(text, 5 * SECOND)
}

private fun CSView<*>.snackBar(text: String, time: Int) {
    make(view, text, time).show()
}

private fun CSView<*>.snackBar(text: String, backColor: Int, textColor: Int) {
    snackBar(text, backColor, textColor, 5 * SECOND)
}

private fun CSView<*>.snackBar(text: String, backColor: Int, textColor: Int, time: Int) {
    make(view, text, time).apply {
        view.setBackgroundColor(backColor)
        (view.findViewById(id.snackbar_text) as? TextView)?.setTextColor(textColor)
    }.show()
}

fun CSView<*>.snackBarWarn(text: String) {
    snackBar(text, color(R.color.cs_yellow), color(R.color.cs_black))
}

fun CSView<*>.snackBarError(text: String) {
    snackBar(text, color(R.color.cs_red), color(R.color.cs_white))
}

fun CSView<*>.snackBarInfo(text: String) {
    snackBar(text, color(R.color.cs_blue), color(R.color.cs_white))
}