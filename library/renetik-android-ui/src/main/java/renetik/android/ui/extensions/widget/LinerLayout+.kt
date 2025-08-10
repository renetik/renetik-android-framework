package renetik.android.ui.extensions.widget

import android.content.Context
import android.widget.LinearLayout
import renetik.android.ui.protocol.CSViewInterface

fun HorizontalLayout(context: Context) = LinearLayout(context).also {
    it.orientation = LinearLayout.HORIZONTAL
}

fun VerticalLayout(context: Context) = LinearLayout(context).also {
    it.orientation = LinearLayout.VERTICAL
}

fun CSViewInterface.verticalLayout(init: (LinearLayout).() -> Unit) =
    VerticalLayout(context).apply { init(this) }


fun CSViewInterface.horizontalLayout(init: (LinearLayout).() -> Unit) =
    HorizontalLayout(context).apply { init(this) }

