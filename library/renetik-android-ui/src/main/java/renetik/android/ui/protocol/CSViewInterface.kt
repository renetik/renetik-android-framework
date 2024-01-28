package renetik.android.ui.protocol

import android.view.View
import androidx.annotation.StyleRes
import renetik.android.event.common.CSHasContext

interface CSViewInterface : CSHasContext {
    companion object {
        @StyleRes
        var themeOverride: Int? = null
    }

    val view: View
}