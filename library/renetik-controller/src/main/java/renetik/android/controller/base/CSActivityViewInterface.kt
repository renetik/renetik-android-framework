package renetik.android.controller.base

import renetik.android.framework.event.CSEvent
import renetik.android.framework.event.CSViewInterface
import renetik.android.framework.event.CSVisibility
import renetik.android.framework.lang.property.CSProperty


//TODO: This way Activity implements CSVisibility what doesn't make sense,
// and if does the why not also CSVisibleEventOwner
interface CSActivityViewInterface : CSViewInterface, CSVisibility {
    val eventResume: CSEvent<Unit>
    val eventPause: CSEvent<Unit>
    val eventBack: CSEvent<CSProperty<Boolean>>
    fun activity(): CSActivity
}