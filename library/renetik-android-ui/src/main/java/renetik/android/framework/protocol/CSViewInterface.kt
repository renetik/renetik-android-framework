package renetik.android.framework.protocol

import android.view.View
import renetik.android.event.owner.CSHasContext

interface CSViewInterface : CSHasContext {
    val view: View
}