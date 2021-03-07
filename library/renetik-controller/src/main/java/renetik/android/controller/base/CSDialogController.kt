package renetik.android.controller.base

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSLayoutRes
import renetik.android.controller.R
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.event.listenOnce

open class CSDialogController<ViewType : View> : CSViewController<ViewType> {

    protected lateinit var dialog: Dialog
    private var cancelableOnTouchOutside = true

    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    constructor(parent: CSViewController<out ViewGroup>, layoutRes: CSLayoutRes) :
            super(parent, layoutRes)

    constructor(layoutRes: CSLayoutRes) : this(navigation, layoutRes)

    open fun show() = apply {
        val builder = prepareDialog()
        showDialog(builder)
    }

    fun show(@StringRes positiveTitle: Int = R.string.cs_dialog_ok,
             onPositive: () -> Unit) = apply {
        val builder = prepareDialog()
        builder.setPositiveButton(positiveTitle) { _, _ -> onPositive() }
        showDialog(builder)
    }

    fun show(@StringRes positiveTitle: Int = R.string.cs_dialog_ok,
             onPositive: () -> Unit,
             @StringRes negativeTitle: Int = R.string.cs_dialog_cancel,
             onNegative: () -> Unit) = apply {
        val builder = prepareDialog()
        builder.setPositiveButton(positiveTitle) { _, _ -> onPositive() }
        builder.setNegativeButton(negativeTitle) { _, _ -> onNegative() }
        showDialog(builder)
    }

    fun showFullScreen() = apply {
        dialog = Dialog(context, R.style.CSFullScreenDialogStyle).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawableResource(R.color.cs_transparent)
            setContentView(view)
            setOnDismissListener {
                if (!isDestroyed) {
                    lifecycleDeInitialize()
                    eventOnDismiss.fire()
                }
            }
            show()
            lifecycleInitialize()
        }
    }

    fun hide() = dialog.dismiss()

    fun cancelableOnTouchOutside(cancelable: Boolean) = apply {
        cancelableOnTouchOutside = cancelable
    }

    private fun prepareDialog(): MaterialAlertDialogBuilder {
        val dialog = MaterialAlertDialogBuilder(this).setView(view).setOnDismissListener {
            if (!isDestroyed) {
                lifecycleDeInitialize()
                eventOnDismiss.fire()
            }
        }
        lifecycleInitialize()
        return dialog
    }

    open fun showDialog(builder: MaterialAlertDialogBuilder) {
        dialog = builder.show()
        dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}