package renetik.android.controller.base

import android.view.KeyEvent
import renetik.java.lang.CSValue

class CSOnKeyDownResult(val keyCode: Int, val event: KeyEvent) {
    val returnValue = CSValue(false)
}