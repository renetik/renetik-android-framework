package renetik.android.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import renetik.android.content.applicationIcon
import renetik.android.content.applicationLogo
import renetik.android.content.attributeColor
import renetik.android.content.inflate
import renetik.android.framework.CSContext
import renetik.kotlin.asString
import renetik.kotlin.notNull
import renetik.android.primitives.isSet
import renetik.android.view.button
import renetik.android.view.extensions.*
import renetik.android.view.onClick
import renetik.android.view.textView
import renetik.android.widget.text
import renetik.android.widget.withClear


class CSDialog : CSContext {

    companion object {
        private var initDefaultsFunction: (CSDialog.() -> Unit)? = null
        fun defaults(function: CSDialog.() -> Unit) = apply { initDefaultsFunction = function }
    }

    constructor(activity: Activity) : super(activity)
    constructor(view: View) : super(view.context)

    private var materialDialog: MaterialDialog? = null
    private var dialog: Dialog? = null
    private var title: String? = null
    private var message: String? = null
    private var view: View? = null
    private var isShowAppIcon = true
    private var isCancelable = true
    private var icon: Drawable? = null
    private var progressBar: ProgressBar? = null
    var isCanceled = false
    val notCanceled get() = !isCanceled
    var isCheckboxChecked = false
    var onDialogCancel: ((CSDialog) -> Unit)? = null

    val inputText get() = materialDialog?.getInputField()?.text.asString

    init {
        initDefaultsFunction?.invoke(this)
    }

    fun title(value: String) = apply { title = value }
    fun message(value: String) = apply { message = value }
    fun text(title: String, message: String) = title(title).message(message)

    fun show() = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
        }
    }

    private fun MaterialDialog.initialize() {
        title?.let { title(text = it) }
        message?.let { message(text = it) }
        if (isShowAppIcon) icon?.let { icon -> icon(drawable = icon) }
            ?: icon(drawable = applicationLogo ?: applicationIcon)
        onCancel {
            isCanceled = true
            onDialogCancel?.invoke(this@CSDialog)
        }
        cancelable(isCancelable)
    }

    fun show(positiveText: String, onPositive: (CSDialog) -> Unit) = apply {
        materialDialog = MaterialDialog(this).show {
            positiveButton(text = positiveText) { onPositive(this@CSDialog) }
            if (isCancelable) negativeButton(R.string.cs_dialog_cancel)
            initialize()
        }
    }

    fun show(
        negativeText: String? = null,
        negativeAction: ((CSDialog) -> Unit)? = null,
        positiveText: String? = null,
        positiveAction: (CSDialog) -> Unit,
    ) = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
            if (negativeText.notNull || negativeAction.notNull || isCancelable)
                negativeButton(text = negativeText ?: getString(R.string.cs_dialog_cancel)) {
                    negativeAction?.let { it(this@CSDialog) }
                }
            positiveButton(text = positiveText ?: getString(R.string.cs_dialog_ok)) {
                positiveAction(this@CSDialog)
            }
        }
    }

    fun showChoice(
        leftButton: String, leftButtonAction: (CSDialog) -> Unit,
        rightButton: String, rightButtonAction: (CSDialog) -> Unit
    ) = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
            @Suppress("DEPRECATION")
            neutralButton(text = leftButton) { leftButtonAction(this@CSDialog) }
            positiveButton(text = rightButton) { rightButtonAction(this@CSDialog) }
        }
    }

    fun showProgress(
        title: String? = null,
        cancelable: Boolean = true,
        cancelTitle: String = getString(R.string.cs_dialog_cancel),
        onCancel: ((CSDialog) -> Unit)? = null
    ) = apply {
        progressBar = ProgressBar(this).apply { isIndeterminate = true }
        dialog = Dialog(context, R.style.CSProgressDialogStyle).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawableResource(R.color.cs_transparent)
            setContentView(inflate<View>(R.layout.progress).apply {
                textView(R.id.progress_title).text(title ?: "")
                button(R.id.progress_button_cancel).text(cancelTitle).onClick {
                    this@CSDialog.hide()
                    onCancel?.invoke(this@CSDialog)
                }.shownIf(cancelable)
                backgroundRoundedWithColor(attributeColor(R.attr.colorSurface), 10f)
            })
            show()
        }
    }

    fun showProgress(
        progressMax: Int, cancelTitle: String = getString(R.string.cs_dialog_cancel),
        onCancel: ((CSDialog) -> Unit)? = null
    ) = apply {
        progressBar = ProgressBar(this).apply {
            isIndeterminate = false
            max = progressMax
        }
        materialDialog = MaterialDialog(this).show {
            initialize()
            customView(view = progressBar, scrollable = false)
            noAutoDismiss()
            cancelable(false)
            negativeButton(text = cancelTitle) {
                dismiss()
                onCancel?.invoke(this@CSDialog)
            }
        }
    }

    fun showInput(hint: String = "", text: String = "", positiveAction: (CSDialog) -> Unit) =
        apply {
            materialDialog = MaterialDialog(this).show {
                initialize()
                positiveButton(R.string.cs_dialog_ok)
                if (isCancelable) negativeButton(R.string.cs_dialog_cancel)
                input(hint = hint, prefill = text, allowEmpty = false) { _, _ ->
                    positiveAction(this@CSDialog)
                }
                getInputField().withClear()
            }
        }

    fun cancelable(cancelable: Boolean) = apply {
        isCancelable = cancelable
    }

    fun withIcon(showAppIcon: Boolean) = apply { isShowAppIcon = showAppIcon }

    fun withIcon(@DrawableRes resource: Int) = apply { icon = getDrawable(resource) }

    fun onCancel(cancelAction: (CSDialog) -> Unit) = apply { onDialogCancel = cancelAction }

    fun hide() = apply { materialDialog?.dismiss() ?: dialog?.dismiss() }

    class CSOnDialogViewAction<ViewType : View>(val dialog: CSDialog, val view: ViewType)

    fun <ViewType : View> showView(
        view: ViewType,
        action: ((CSOnDialogViewAction<ViewType>) -> Boolean)? = null
    ): View {
        if (title.isSet and message.isSet) throw UnsupportedOperationException("No place for second text with custom view")
        this.view = view
        materialDialog = MaterialDialog(this).show {
            initialize()
            customView(view = view, scrollable = true)
            if (action.notNull) noAutoDismiss()
            action?.let {
                positiveButton(R.string.cs_dialog_ok) {
                    if (action(
                            CSOnDialogViewAction(
                                this@CSDialog,
                                view
                            )
                        )
                    ) materialDialog!!.dismiss()
                }
            }
        }
        return view
    }

    fun checkBox(title: String, checked: Boolean, onChecked: ((CSDialog) -> Void)? = null) =
        apply {
            isCheckboxChecked = checked
            MaterialDialog(this).show {
                initialize()
                checkBoxPrompt(text = title, isCheckedDefault = checked) { checked ->
                    isCheckboxChecked = checked
                    onChecked?.invoke(this@CSDialog)
                }
            }
        }

    fun progress(value: Int): CSDialog {
        progressBar!!.progress = value
        return this
    }
}


fun <ViewType : View> CSDialog.showViewOf(
    @LayoutRes layoutId: Int,
    action: ((CSDialog.CSOnDialogViewAction<View>) -> Boolean)? = null
) = showView(inflate<ViewType>(layoutId), action)

fun CSDialog.showView(
    @LayoutRes layoutId: Int,
    action: ((CSDialog.CSOnDialogViewAction<View>) -> Boolean)? = null
) = showViewOf<View>(layoutId, action)