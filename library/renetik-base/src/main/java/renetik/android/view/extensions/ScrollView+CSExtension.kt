package renetik.android.view.extensions

import android.view.View
import android.widget.ScrollView

fun ScrollView.scrollToChild(child: View, centered: Boolean) {
    afterLayout {
        smoothScrollTo(0, child.y.toInt() -
                if (centered) ((height / 2) - (child.height / 2)) else 0)
    }
}