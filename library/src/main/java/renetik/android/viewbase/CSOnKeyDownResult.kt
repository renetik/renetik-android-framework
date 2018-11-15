package renetik.android.viewbase

import android.view.KeyEvent
import renetik.android.java.lang.CSValue

class CSOnKeyDownResult(val keyCode: Int, val event: KeyEvent) {
    val returnValue = CSValue(false)
}