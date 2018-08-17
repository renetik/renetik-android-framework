package cs.android.view

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import cs.android.R
import cs.android.viewbase.CSContextController


class CSDialog(context: Context) : CSContextController(context) {

    private val builder = MaterialDialog.Builder(context());

    fun title(title: String): CSDialog {
        builder.title(title)
        return this
    }

    fun message(message: String): CSDialog {
        builder.content(message)
        return this
    }

    fun text(title: String, message: String): CSDialog {
        builder.title(title)
        builder.content(message)
        return this
    }

    fun action(positiveText: String, onPositive: (CSDialog) -> Unit) {
        builder.positiveText(positiveText)
                .onPositive { _, _ -> onPositive(this) }
                .negativeText(R.string.cs_dialog_cancel).show()
    }

    fun action(onPositive: (CSDialog) -> Unit) {
        builder.positiveText(R.string.cs_dialog_ok)
                .negativeText(R.string.cs_dialog_cancel).onPositive { _, _ -> onPositive(this) }
                .show()
    }

    fun show() {
        builder.positiveText(R.string.cs_dialog_ok).show()
    }

    fun choice(leftButton: String, leftButtonAction: (CSDialog) -> Unit,
               rightButton: String, rightButtonAction: (CSDialog) -> Unit) {
        builder.neutralText(leftButton).onNeutral { _, _ -> leftButtonAction(this) }
                .positiveText(rightButton).onPositive { _, _ -> rightButtonAction(this) }.show()
    }

    fun action(positiveText: String, positiveAction: (CSDialog) -> Unit
               , negativeText: String, negativeAction: (CSDialog) -> Unit) {
        builder.positiveText(positiveText).onPositive { _, _ -> positiveAction(this) }
                .negativeText(negativeText).onNeutral { _, _ -> negativeAction(this) }.show()
    }
}
