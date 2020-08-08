package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSLayoutId
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.event.event
import renetik.android.java.event.fire

open class CSDialogController<ViewType : View> : CSViewController<ViewType> {

    var dialog: AlertDialog? = null
    private var cancelableOnTouchOutside = true
    val eventOnDismiss = event<Unit>()

    constructor(parent: CSViewController<out ViewGroup>, layoutId: CSLayoutId? = null) :
            super(parent, layoutId)

    constructor(layoutId: CSLayoutId? = null) : this(navigation, layoutId)

    fun show() {
        lifecycleInitialize()
        val builder = MaterialAlertDialogBuilder(this).setView(view)
        builder.setOnDismissListener {
            if (!isDestroyed) {
                lifecycleDeInitialize()
                eventOnDismiss.fire()
            }
        }
        dialog = builder.show()
        dialog!!.setCanceledOnTouchOutside(cancelableOnTouchOutside)
    }

    fun showFullScreen() {
        show()
        dialog!!.window!!.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    fun hide() = dialog!!.dismiss()

    fun cancelableOnTouchOutside(cancelable: Boolean) = apply {
        cancelableOnTouchOutside = cancelable
        dialog?.setCanceledOnTouchOutside(cancelableOnTouchOutside)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }
}