package renetik.android.view.menu

import renetik.android.view.base.CSView
import renetik.java.lang.CSLang

class CSCustomMenuItem(val index: Int, var title: String? = null) {
    var iconResource: Int = 0
    var onClick: ((CSCustomMenuItem) -> Unit)? = null
    var note: String? = null
    var isCloseMenu = CSLang.YES
    var customView: CSView<*>? = null
}
