package renetik.android.controller.base

import android.view.KeyEvent
import renetik.android.framework.event.property.CSEventPropertyFunctions.property

class CSOnKeyDownResult(val keyCode: Int, val event: KeyEvent) {
    val returnValue = property(false)
}