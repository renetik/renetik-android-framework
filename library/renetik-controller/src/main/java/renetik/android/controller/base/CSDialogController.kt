package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSLayoutId
import renetik.android.controller.common.CSNavigationInstance.navigation

open class CSDialogController<ViewType : View> : CSViewController<ViewType> {

    lateinit var dialog: AlertDialog

    constructor(parent: CSViewController<out ViewGroup>, layoutId: CSLayoutId? = null) :
            super(parent, layoutId)

    constructor(layoutId: CSLayoutId? = null) : this(navigation, layoutId)

    fun show() {
        initialize()
        val builder = MaterialAlertDialogBuilder(this).setView(view)
        builder.setOnDismissListener { deInitialize() }
        dialog = builder.show()
    }

    fun showFullScreen() {
        show()
        dialog.window!!.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    fun hide() = dialog.dismiss()
}