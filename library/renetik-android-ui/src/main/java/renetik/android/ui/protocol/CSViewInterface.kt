package renetik.android.ui.protocol

import android.view.View
import renetik.android.event.common.CSHasContext

interface CSViewInterface : CSHasContext {
    var themeOverride: Int?
    val view: View
}