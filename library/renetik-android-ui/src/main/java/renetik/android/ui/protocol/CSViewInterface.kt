package renetik.android.ui.protocol

import android.view.View
import renetik.android.event.common.CSHasContext

interface CSViewInterface : CSHasContext {
    val view: View
}