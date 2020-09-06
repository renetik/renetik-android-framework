package renetik.android.controller.base

import android.view.KeyEvent
import renetik.android.java.common.CSProperty

class CSOnKeyDownResult(val keyCode: Int, val event: KeyEvent) {
    val returnValue = CSProperty(false)
}