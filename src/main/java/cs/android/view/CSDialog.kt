package cs.android.view

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import cs.android.CSContextInterface
import cs.android.R
import cs.android.R.string.cs_dialog_ok
import cs.android.viewbase.CSContextController
import cs.java.callback.CSRun
import cs.java.callback.CSRunWith
import cs.java.callback.CSRunWithWith
import cs.java.lang.CSLang.*

class CSDialog(context: Context) : CSContextController(context) {

    private val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog? = null
    private var inputField: EditText? = null
    private var onDismiss: CSRunWith<AlertDialog>? = null
    private var cancelable = YES

    constructor(hasContext: CSContextInterface) : this(hasContext.context())

    fun create(message: Int, run: CSRun): CSDialog {
        return create(0, message, cs_dialog_ok, R.string.cs_dialog_cancel, CSRunWith {
            if (it == cs_dialog_ok) run.run()
            hideKeyboard()
        })
    }

    fun show(message: Int, run: CSRun): CSDialog {
        return create(message, run).show()
    }

    fun create(title: Int, message: Int, ok: Int, cancel: Int, call: CSRunWith<Int>): CSDialog {
        return create(getString(title), getString(message), ok, cancel, call)
    }

    fun create(title: String, message: String, dialogOk: Int, dialogCancel: Int, call: CSRunWith<Int>): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, call)
        return this
    }

    fun create(title: String, arrayId: Int, dialogCancel: String, runWith: CSRunWith<Int>) {
        text(title, "")
        builder.setNegativeButton(dialogCancel, null)
        builder.setItems(arrayId) { _, which -> runWith.run(which) }
    }

    fun create(title: String, items: List<String>, dialogCancel: String, runWith: CSRunWith<Int>): CSDialog {
        text(title, "")
        builder.setNegativeButton(dialogCancel, null)
        builder.setItems(items.toTypedArray<CharSequence>()) { _, which -> runWith.run(which) }
        return this
    }

    fun text(title: String?, message: String?): CSDialog {
        if (set(title)) builder.setTitle(title)
        if (set(message)) builder.setMessage(message)
        return this
    }

    fun buttons(dialogOk: Int, call: CSRunWith<Int>): CSDialog {
        builder.setPositiveButton(dialogOk) { _, _ ->
            call.run(dialogOk)
            hideKeyboard()
        }
        return this
    }

    fun buttons(dialogOk: Int, dialogCancel: Int, call: CSRunWith<Int>) {
        builder.setPositiveButton(dialogOk) { _, _ ->
            call.run(dialogOk)
            hideKeyboard()
        }
        if (set(dialogCancel))
            builder.setNegativeButton(dialogCancel) { _, _ ->
                call.run(dialogCancel)
                hideKeyboard()
            }
    }

    fun create(title: String, message: String, dialogOk: String, dialogCancel: String, call: CSRunWithWith<String, CSDialog>): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, call)
        return this
    }

    fun buttons(dialogOk: String, call: CSRunWith<String>): CSDialog {
        builder.setPositiveButton(dialogOk) { _, _ ->
            call.run(dialogOk)
            hideKeyboard()
        }
        return this
    }

    fun buttons(dialogOk: String, dialogCancel: String?, call: CSRunWithWith<String, CSDialog>?): CSDialog {
        builder.setPositiveButton(dialogOk) { _, _ ->
            call?.run(dialogOk, this)
            hideKeyboard()
        }
        if (set(dialogCancel)) builder.setNegativeButton(dialogCancel) { _, _ ->
            call?.run(dialogCancel, this)
            hideKeyboard()
        }
        return this
    }

    fun hideKeyboard() {
        dialog?.currentFocus?.let { hideKeyboard(it.windowToken) }
    }

    fun create(title: String, message: String, dialogOk: String, dialogCancel: String, neutral: String, call: CSRunWithWith<String, CSDialog>): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, neutral, call)
        return this
    }

    fun buttons(dialogOk: String, dialogCancel: String, neutral: String, call: CSRunWithWith<String, CSDialog>?): CSDialog {
        buttons(dialogOk, dialogCancel, call)
        builder.setNeutralButton(neutral) { _, _ ->
            call?.run(neutral, this)
            hideKeyboard()
        }
        return this
    }

    fun icon(icon: Int): CSDialog {
        builder.setIcon(icon)
        return this
    }

    fun inputFieldText(): String {
        return inputField().text.toString()
    }

    fun inputField(): EditText {
        if (no(inputField)) inputField = EditText(context())
        builder.setView(inputField)
        return inputField!!
    }

    fun text(title: Int, message: Int) {
        if (set(title)) builder.setTitle(title)
        if (set(message)) builder.setMessage(message)
    }

    fun text(string: String): CSDialog {
        return text(string, "")
    }

    fun show(title: Int, message: Int, ok: Int, cancel: Int, call: CSRunWith<Int>): CSDialog {
        return show(getString(title), getString(message), ok, cancel, call)
    }

    fun show(title: String?, message: String?, dialogOk: Int, dialogCancel: Int, call: CSRunWith<Int>): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, call)
        show()
        return this
    }

    fun show(title: String?, message: String?, dialogOk: Int, onOkClick: CSRun, dialogCancel: Int): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, CSRunWith { value -> if (dialogOk == value) onOkClick.run() })
        show()
        return this
    }

    fun show(): CSDialog {
        dialog = builder.show()
        dialog!!.setOnDismissListener { onDismiss?.run(dialog) }
        dialog!!.setCanceledOnTouchOutside(cancelable)
        dialog!!.setCancelable(cancelable)
        return this
    }

    fun hide(): CSDialog {
        dialog?.dismiss()
        return this
    }

    fun show(title: String, message: String, dialogOK: String, call: CSRunWithWith<String, CSDialog>): CSDialog {
        text(title, message)
        buttons(dialogOK, null, call)
        show()
        return this
    }

    fun show(title: String, message: String, dialogOk: String, dialogCancel: String, call: CSRunWithWith<String, CSDialog>): CSDialog {
        text(title, message)
        buttons(dialogOk, dialogCancel, call)
        show()
        return this
    }

    fun inputType(type: Int): CSDialog {
        inputField().inputType = type
        return this
    }

    fun cancelable(cancelable: Boolean): CSDialog {
        this.cancelable = cancelable
        return this
    }

    fun onDismiss(onDismiss: CSRunWith<AlertDialog>): CSDialog {
        this.onDismiss = onDismiss
        return this
    }


    fun inputFieldText(textValue: String): CSDialog {
        inputField().setText(textValue)
        return this
    }

    fun show(title: String?, message: String?, function: Function0<Unit>?) = show(title, message,
            cs_dialog_ok, R.string.cs_dialog_cancel, CSRunWith { value -> if (cs_dialog_ok == value) function?.invoke() })

    fun show(title: String?, function: Function0<Unit>? = null) = show(title, null, cs_dialog_ok, R.string.cs_dialog_cancel,
            CSRunWith { value -> if (cs_dialog_ok == value) function?.invoke() })

    fun showMessage(message: String?) = show(null, message, cs_dialog_ok, CSRun {}, 0)
}
