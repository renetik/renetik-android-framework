package renetik.android.view.menu

import renetik.android.base.CSView

class CSCustomMenuItem(val index: Int, var title: String? = null) {
    var iconResource: Int = 0
    var onClick: ((CSCustomMenuItem) -> Unit)? = null
    var note: String? = null
    var isCloseMenu = true
    var customView: CSView<*>? = null
}
