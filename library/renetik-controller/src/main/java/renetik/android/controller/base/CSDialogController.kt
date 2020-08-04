package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSLayoutId
import renetik.android.base.CSView
import renetik.android.controller.common.CSNavigationInstance.navigation

open class CSDialogController<ViewType : View> : CSViewController<ViewType> {

    lateinit var dialog: AlertDialog

    constructor(parent: CSViewController<out ViewGroup>, layoutId: CSLayoutId? = null) :
            super(parent, layoutId)

    constructor(layoutId: CSLayoutId? = null) : this(navigation, layoutId)

    override fun show(): CSView<ViewType> {
        initialize()
        val builder = MaterialAlertDialogBuilder(this).setView(view)
        builder.setOnDismissListener { deInitialize() }
        dialog = builder.show()
        return this
    }

    override fun hide(): CSView<ViewType> {
        dialog.hide()
        return this
    }

}