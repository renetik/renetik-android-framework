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
import renetik.android.base.CSContextController
import renetik.android.base.CSView
import renetik.android.extensions.applicationIcon
import renetik.android.extensions.applicationLogo
import renetik.android.extensions.inflate
import renetik.android.java.common.CSName
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.*


class CSDialog : CSContextController {

    companion object {
        private var initDefaultsFunction: (CSDialog.() -> Unit)? = null
        fun defaults(function: CSDialog.() -> Unit) = apply { initDefaultsFunction = function }
    }

    constructor(activity: Activity) : super(activity)
    constructor(view: CSView<*>) : super(view)
    constructor(view: View) : super(view.context)

    private var materialDialog: MaterialDialog? = null
    private var dialog: Dialog? = null
    private var title: String? = null
    private var message: String? = null
    private var view: View? = null
    private var isShowAppIcon = true
    private var isCancelable = true
    private var icon: Drawable? = null
    private var progress: ProgressBar? = null
    var isCanceled = false
    val notCanceled get() = !isCanceled
    var isCheckboxChecked = false
    var onDialogCancel: ((CSDialog) -> Unit)? = null

    val inputText: String
        get() {
            val text = materialDialog?.getInputField()?.text
            val name = text as? CSName  //TODO fix this
            return name?.name ?: text?.toString() ?: ""
        }

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
//            onPreShow { dialog -> }
//            onShow { dialog -> }
//            onDismiss { dialog -> }
    }

//    private fun styleDialogBuilder() {
//        builder.titleColorAttr(R.attr.colorOnSurface)
//            .contentColorAttr(R.attr.colorPrimaryVariant)  //title,textfield color
//            .linkColorAttr(R.attr.colorSecondaryVariant)  // notice attr is used instead of none or res for attribute resolving
//            .dividerColorAttr(R.attr.colorSecondaryVariant)
//            .backgroundColorAttr(R.attr.colorSurface)
//            .positiveColor(colorFromAttribute(R.attr.colorPrimary))
//            .neutralColorAttr(R.attr.colorPrimary)
//            .negativeColorAttr(R.attr.colorOnSurface)
//            .widgetColorAttr(R.attr.colorPrimaryVariant) //textField line
//            .buttonRippleColorAttr(R.attr.colorPrimaryVariant)
//    }

    fun show(positiveText: String, onPositive: (CSDialog) -> Unit) = apply {
        materialDialog = MaterialDialog(this).show {
            positiveButton(text = positiveText) { onPositive(this@CSDialog) }
            if (isCancelable) negativeButton(R.string.cs_dialog_cancel)
            initialize()
        }
    }

    fun show(
        positiveText: String, onPositive: (CSDialog) -> Unit,
        onNegative: (CSDialog) -> Unit
    ) = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
            positiveButton(text = positiveText) { onPositive(this@CSDialog) }
            negativeButton(R.string.cs_dialog_cancel) { onNegative(this@CSDialog) }
        }
    }

    fun show(onPositive: (CSDialog) -> Unit) = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
            positiveButton(R.string.cs_dialog_ok) { onPositive(this@CSDialog) }
            if (isCancelable) negativeButton(R.string.cs_dialog_cancel)
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

    fun show(
        positiveText: String, positiveAction: (CSDialog) -> Unit
        , negativeText: String, negativeAction: (CSDialog) -> Unit
    ) = apply {
        materialDialog = MaterialDialog(this).show {
            initialize()
            positiveButton(text = positiveText) { positiveAction(this@CSDialog) }
            negativeButton(text = negativeText) { negativeAction(this@CSDialog) }
        }
    }

    fun showProgress(
        title: String? = null,
        cancelable: Boolean = true,
        cancelTitle: String = getString(R.string.cs_dialog_cancel),
        onCancel: ((CSDialog) -> Unit)? = null
    ) = apply {
        progress = ProgressBar(this).apply { isIndeterminate = true }
        dialog = Dialog(context, R.style.CSProgressDialogStyle).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawableResource(R.color.cs_transparent)
            setContentView(inflate<View>(R.layout.progress).apply {
                textView(R.id.progress_title).title(title ?: "")
                button(R.id.progress_button_cancel).title(cancelTitle).onClick {
                    this@CSDialog.hide()
                    onCancel?.invoke(this@CSDialog)
                }.shownIf(cancelable)
                backgroundRoundedWithColor(R.attr.colorSurface, 10f)
            })
            show()
        }
    }

    fun showProgress(
        progressMax: Int, cancelTitle: String = getString(R.string.cs_dialog_cancel),
        onCancel: ((CSDialog) -> Unit)? = null
    ) = apply {
        progress = ProgressBar(this).apply {
            isIndeterminate = false
            max = progressMax
        }
        materialDialog = MaterialDialog(this).show {
            initialize()
            customView(view = progress, scrollable = false)
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
                positiveButton(R.string.cs_dialog_ok) { positiveAction(this@CSDialog) }
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
                    if (action(CSOnDialogViewAction(this@CSDialog,
                            view))) materialDialog!!.dismiss()
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
        progress!!.progress = value
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