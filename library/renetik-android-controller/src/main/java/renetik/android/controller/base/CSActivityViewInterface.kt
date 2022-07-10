package renetik.android.controller.base

import renetik.android.event.CSEvent
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.protocol.CSVisibility
import renetik.android.core.lang.variable.CSVariable


//TODO: This way Activity implements CSVisibility what doesn't make sense,
// and if does the why not also CSVisibleEventOwner
interface CSActivityViewInterface : CSViewInterface, CSVisibility {
    val eventResume: CSEvent<Unit>
    val eventPause: CSEvent<Unit>
    val eventBack: CSEvent<CSVariable<Boolean>>
    fun activity(): CSActivity
}