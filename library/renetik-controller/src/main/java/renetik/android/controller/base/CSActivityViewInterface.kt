package renetik.android.controller.base

import renetik.android.framework.event.CSEvent
import renetik.android.framework.event.CSViewInterface
import renetik.android.framework.event.CSVisibility
import renetik.android.framework.lang.CSProperty


//TODO: This way Activity implements CSVisibility what doesn't make sense,
// and if does the why not also CSVisibleEventOwner
interface CSActivityViewInterface : CSViewInterface, CSVisibility {
    val onResume: CSEvent<Unit>
    val onPause: CSEvent<Unit>
    val onBack: CSEvent<CSProperty<Boolean>>
    fun activity(): CSActivity
}