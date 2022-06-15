package renetik.android.framework.protocol

import android.view.View
import renetik.android.framework.protocol.CSHasContext

interface CSViewInterface : CSHasContext {
    val view: View
}