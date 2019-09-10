package renetik.android.controller.base

import android.view.KeyEvent
import renetik.android.java.common.CSValue

class CSOnKeyDownResult(val keyCode: Int, val event: KeyEvent) {
    val returnValue = CSValue(false)
}