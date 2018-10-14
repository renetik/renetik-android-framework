package cs.android.view

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import cs.android.R
import cs.android.extensions.string
import cs.android.viewbase.CSContextController


class CSDialog(context: Context) : CSContextController(context) {

    private val builder = MaterialDialog.Builder(context());
    private var dialog: MaterialDialog? = null

    fun title(title: String): CSDialog {
        builder.title(title)
        return this
    }

    fun message(message: String) = apply {
        builder.content(message)
    }

    fun text(title: String, message: String) = apply {
        builder.title(title)
        builder.content(message)
    }

    fun show() = apply { dialog = builder.show() }

    fun show(positiveText: String, onPositive: (CSDialog) -> Unit) = apply {
        builder.positiveText(positiveText).onPositive { _, _ -> onPositive(this) }
                .negativeText(R.string.cs_dialog_cancel)
    }.show()

    fun show(onPositive: (CSDialog) -> Unit) = apply {
        builder.positiveText(R.string.cs_dialog_ok).onPositive { _, _ -> onPositive(this) }
                .negativeText(R.string.cs_dialog_cancel)
    }.show()

    fun showChoice(leftButton: String, leftButtonAction: (CSDialog) -> Unit,
                   rightButton: String, rightButtonAction: (CSDialog) -> Unit) = apply {
        builder.neutralText(leftButton).onNeutral { _, _ -> leftButtonAction(this) }
                .positiveText(rightButton).onPositive { _, _ -> rightButtonAction(this) }
    }.show()

    fun show(positiveText: String, positiveAction: (CSDialog) -> Unit
             , negativeText: String, negativeAction: (CSDialog) -> Unit) = apply {
        builder.positiveText(positiveText).onPositive { _, _ -> positiveAction(this) }
                .negativeText(negativeText).onNegative { _, _ -> negativeAction(this) }
    }.show()

    fun showIndeterminateProgress(onCancel: (CSDialog) -> Unit) = apply {
        builder.progress(true, 0).negativeText(R.string.cs_dialog_cancel).cancelable(false)
                .onNegative { _, _ -> onCancel(this) }
    }.show()

    fun showInput(hint: String, value: String, positiveAction: (CSDialog) -> Unit) = apply {
        builder.positiveText(R.string.cs_dialog_ok)
                .input(hint, value, false) { _, _ -> positiveAction(this) }
    }.show()

    fun inputValue(): String = string(dialog?.inputEditText?.text)

    fun cancelable(cancelable: Boolean) = apply { builder.cancelable(cancelable) }

    fun onCancel(cancelAction: (CSDialog) -> Unit) = apply {
        builder.cancelListener { cancelAction(this) }
    }

    fun hide() = apply { dialog?.hide() }
}
