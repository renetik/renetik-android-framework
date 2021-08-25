package renetik.android.controller.base

import renetik.android.framework.event.CSEvent
import renetik.android.framework.event.CSContextInterface
import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.event.CSViewInterface
import renetik.android.framework.lang.CSProperty

interface CSActivityViewInterface : CSViewInterface {
    val onResume: CSEvent<Unit>
    val onPause: CSEvent<Unit>
    val onBack: CSEvent<CSProperty<Boolean>>
    fun activity(): CSActivity
}