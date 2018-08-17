package cs.android.extensions.view

import android.widget.TextView
import com.google.android.material.R.id
import com.google.android.material.snackbar.Snackbar.make
import cs.android.R
import cs.android.viewbase.CSView
import cs.java.lang.CSLang.SECOND

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
    snackBar(text, color(R.color.yellow), color(R.color.black))
}

fun CSView<*>.snackBarError(text: String) {
    snackBar(text, color(R.color.red), color(R.color.white))
}

fun CSView<*>.snackBarInfo(text: String) {
    snackBar(text, color(R.color.blue), color(R.color.white))
}