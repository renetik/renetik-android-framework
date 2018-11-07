package renetik.android.viewbase.menu

import android.view.MenuItem.*
import renetik.android.viewbase.CSViewController
import renetik.android.lang.CSLang.NO
import renetik.android.lang.CSLang.YES

/**
 * Created by Rene Dohan on 18/1/17.
 */
open class CSMenuItem {

    companion object {
        private var lastGeneratedMenuItemId = 10
    }

    val id: Int
    val isGenerated: Boolean
    private val controller: CSViewController<*>
    var title: String? = null
    var isVisible = YES
    var isChecked: Boolean? = null
    var iconResource = 0
    var showAsAction = SHOW_AS_ACTION_IF_ROOM
    private var runWith: ((CSMenuItem) -> Unit)? = null

    constructor(controller: CSViewController<*>, id: Int) {
        this.controller = controller
        this.id = id
        isGenerated = false
    }

    constructor(controller: CSViewController<*>, title: String) {
        this.controller = controller
        this.id = lastGeneratedMenuItemId++
        isGenerated = true
        this.title = title
    }

    fun onClick(run: (CSMenuItem) -> Unit) = apply { runWith = run }

    fun run() = runWith?.invoke(this)

    fun hide() = visible(NO)

    fun show() = visible(YES)

    fun visible(visible: Boolean) = apply {
        if (isVisible == visible) return this
        isVisible = visible
        if (controller.isCreated) controller.invalidateOptionsMenu()
    }

    fun onChecked(onItem: CSOnMenuItem) {
        isChecked = !isChecked!!
        onItem.isChecked = isChecked!!
        run()
    }

    fun setIconResourceId(iconResourceId: Int) = apply { this.iconResource = iconResourceId }

    fun neverAsAction() = apply { showAsAction = SHOW_AS_ACTION_NEVER }

    fun alwaysAsAction() = apply { showAsAction = SHOW_AS_ACTION_ALWAYS }

    fun withText() = apply { showAsAction = showAsAction or SHOW_AS_ACTION_WITH_TEXT }

}

