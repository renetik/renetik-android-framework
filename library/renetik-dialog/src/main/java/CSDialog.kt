package renetik.android.dialog

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CompoundButton
import com.afollestad.materialdialogs.MaterialDialog
import renetik.android.base.CSContextController
import renetik.android.base.CSView
import renetik.android.extensions.applicationIcon
import renetik.android.extensions.applicationLogo
import renetik.android.extensions.colorFromAttribute
import renetik.android.extensions.inflate
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.string
import renetik.android.view.extensions.withClear

class CSDialog : CSContextController {

    companion object {
        private var initDefaultsFunction: (CSDialog.() -> Unit)? = null
        fun defaults(function: CSDialog.() -> Unit) = apply { initDefaultsFunction = function }
    }

    constructor(activity: Activity) : super(activity)
    constructor(view: CSView<*>) : super(view)
    constructor(view: View) : super(view.context)

    private val builder = MaterialDialog.Builder(this);
    private var dialog: MaterialDialog? = null
    private var title: String? = null
    private var message: String? = null
    private var view: View? = null
    private var isShowAppIcon = true
    private var icon: Drawable? = null
    val isCanceled get() = dialog?.isCancelled ?: false
    val notCanceled get() = !isCanceled
    val isCheckboxChecked get() = dialog?.isPromptCheckBoxChecked ?: false

    init {
        initDefaultsFunction?.invoke(this)
    }

    fun title(value: String) = apply { title = value }
    fun message(value: String) = apply { message = value }
    fun text(title: String, message: String) = title(title).message(message)

    fun checkBox(title: String, checked: Boolean, onChecked: ((CompoundButton) -> Void)? = null) =
            apply { builder.checkBoxPrompt(title, checked) { button, _ -> onChecked?.invoke(button) } }

    fun show() = apply {
        title?.let { builder.title(it) }
        message?.let { builder.content(it) }
        styleDialogBuilder()
        updateIcon()
        dialog = builder.show()
    }

    private fun styleDialogBuilder() {
        builder.titleColorAttr(R.attr.colorOnSurface)
                .contentColorAttr(R.attr.colorPrimaryVariant)  //title,textfield color
                .linkColorAttr(R.attr.colorSecondaryVariant)  // notice attr is used instead of none or res for attribute resolving
                .dividerColorAttr(R.attr.colorSecondaryVariant)
                .backgroundColorAttr(R.attr.colorSurface)
                .positiveColor(colorFromAttribute(R.attr.colorPrimary))
                .neutralColorAttr(R.attr.colorPrimary)
                .negativeColorAttr(R.attr.colorOnSurface)
                .widgetColorAttr(R.attr.colorPrimaryVariant) //textField line
                .buttonRippleColorAttr(R.attr.colorPrimaryVariant)
    }

    private fun updateIcon() {
        if (isShowAppIcon) icon?.let { icon -> builder.icon(icon) }
                ?: let { builder.icon(applicationLogo ?: applicationIcon) }
    }

    fun show(positiveText: String, onPositive: (CSDialog) -> Unit) = apply {
        builder.positiveText(positiveText).onPositive { _, _ -> onPositive(this) }
                .negativeText(R.string.cs_dialog_cancel)
    }.show()

    fun show(positiveText: String, onPositive: (CSDialog) -> Unit,
             onNegative: (CSDialog) -> Unit) = apply {
        builder.positiveText(positiveText).onPositive { _, _ -> onPositive(this) }
                .negativeText(R.string.cs_dialog_cancel).onNegative { _, _ -> onNegative(this) }
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

    fun showIndeterminateProgress(actionText: String, action: (CSDialog) -> Unit,
                                  cancelText: String, onCancel: (CSDialog) -> Unit) = apply {
        builder.positiveText(actionText).onPositive { _, _ -> action(this) }
                .negativeText(cancelText).onNegative { _, _ -> onCancel(this) }
                .progress(true, 0).cancelable(false)
    }.show()

    fun showProgress(progressMax: Int, cancelText: String, onCancel: ((CSDialog) -> Unit)? = null) = apply {
        builder.negativeText(cancelText).onNegative { _, _ -> onCancel?.invoke(this) }
                .progress(false, progressMax).cancelable(false)
    }.show()

    fun showInput(hint: String = "", value: String = "", positiveAction: (CSDialog) -> Unit) = apply {
        builder.positiveText(R.string.cs_dialog_ok)
                .input(hint, value, false) { _, _ -> positiveAction(this) }
                .negativeText(R.string.cs_dialog_cancel)
    }.show().apply { dialog?.inputEditText?.withClear() }

    fun inputValue(): String = string(dialog?.inputEditText?.text)

    fun cancelable(cancelable: Boolean) = apply { builder.cancelable(cancelable) }

    fun withIcon(showAppIcon: Boolean) = apply { isShowAppIcon = showAppIcon }


    fun withIcon(resource: Int) = apply { icon = getDrawable(resource) }

    fun onCancel(cancelAction: (CSDialog) -> Unit) =
            apply { builder.cancelListener { cancelAction(this) } }

    fun hide() = apply { dialog?.dismiss() }

    class CSOnDialogViewAction<ViewType : View>(val dialog: CSDialog, val view: ViewType)

    fun <ViewType : View> showView(view: ViewType,
                                   action: ((CSOnDialogViewAction<ViewType>) -> Boolean)? = null): View {
        if (title.isSet and message.isSet) throw UnsupportedOperationException("No place for second text with custom view")
        title?.let { builder.title(it) } ?: message?.let { builder.title(it) }
        this.view = view
        builder.autoDismiss(action == null).customView(view, true)
        action?.let {
            builder.positiveText(R.string.cs_dialog_ok).onPositive { _, _ ->
                if (action(CSOnDialogViewAction(this, view))) dialog!!.dismiss()
            }
        }
        styleDialogBuilder()
        if (title.isSet or message.isSet) updateIcon()
        dialog = builder.show()
        return view
    }

    fun progress(value: Int): CSDialog {
        dialog!!.setProgress(value)
        return this
    }
}

fun <ViewType : View> CSDialog.showViewOf(layoutId: Int, action: ((CSDialog.CSOnDialogViewAction<View>) -> Boolean)? = null) =
        showView(inflate<ViewType>(layoutId), action)

fun CSDialog.showView(layoutId: Int, action: ((CSDialog.CSOnDialogViewAction<View>) -> Boolean)? = null) =
        showViewOf<View>(layoutId, action)