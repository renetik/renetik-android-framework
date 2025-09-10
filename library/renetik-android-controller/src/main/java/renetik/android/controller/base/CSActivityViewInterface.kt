package renetik.android.controller.base

import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.CSEvent
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.protocol.CSVisibility

interface CSActivityViewInterface : CSViewInterface, CSVisibility {
    val eventResume: CSEvent<Unit>
    val eventPause: CSEvent<Unit>
    val eventBack: CSEvent<CSVariable<Boolean>>
    fun activity(): CSViewActivity<*>
}