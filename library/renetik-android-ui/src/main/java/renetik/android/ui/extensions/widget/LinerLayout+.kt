package renetik.android.ui.extensions.widget

import android.content.Context
import android.widget.LinearLayout

object LinearLayout {
    fun HorizontalLayout(context: Context) =
        LinearLayout(context).also { it.orientation = LinearLayout.HORIZONTAL }
}

