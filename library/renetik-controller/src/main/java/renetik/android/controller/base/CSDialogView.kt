package renetik.android.controller.base

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.controller.R
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.listenOnce
import renetik.android.framework.lang.CSLayoutRes

open class CSDialogView<ViewType : View> : CSActivityView<ViewType> {

    protected lateinit var dialog: Dialog
    private var cancelableOnTouchOutside = true
    private val eventOnDismiss = event<Unit>()
    fun onDismiss(function: () -> Unit) = eventOnDismiss.listenOnce { function() }

    constructor(parent: CSActivityView<out ViewGroup>, layoutRes: CSLayoutRes) :
            super(parent, layoutRes)

    constructor(layoutRes: CSLayoutRes) : this(navigation, layoutRes)

    fun cancelOnTouchOutside(cancelable: Boolean = true) = apply {
        cancelableOnTouchOutside = cancelable
    }

    fun show() = apply {
        showDialog(prepareAlertDialog())
    }

    fun show(
        @StringRes positiveTitle: Int = R.string.cs_dialog_ok,
        onPositive: () -> Unit
    ) = apply {
        val builder = prepareAlertDialog()
        builder.setPositiveButton(positiveTitle) { _, _ -> onPositive() }
        showDialog(builder)
    }

    fun dismiss() = dialog.dismiss()

    fun show(
        @StringRes positiveTitle: Int = R.string.cs_dialog_ok,
        onPositive: () -> Unit,
        @StringRes negativeTitle: Int = R.string.cs_dialog_cancel,
        onNegative: () -> Unit
    ) = apply {
        val builder = prepareAlertDialog()
        builder.setPositiveButton(positiveTitle) { _, _ -> onPositive() }
        builder.setNegativeButton(negativeTitle) { _, _ -> onNegative() }
        showDialog(builder)
    }

    private fun prepareAlertDialog() = MaterialAlertDialogBuilder(this).apply {
        setView(view)
        setOnDismissListener {
            if (!isDestroyed) {
                lifecycleStop()
                eventOnDismiss.fire()
            }
        }
        lifecycleUpdate()
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