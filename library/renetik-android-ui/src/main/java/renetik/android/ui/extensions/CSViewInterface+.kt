package renetik.android.ui.extensions

import renetik.android.ui.extensions.view.rectangleInWindow
import renetik.android.ui.protocol.CSViewInterface

val CSViewInterface.leftMarginInWindow: Int
    get() = view.rectangleInWindow.left