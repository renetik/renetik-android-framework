package renetik.android.controller.base

import renetik.android.framework.event.CSEvent
import renetik.android.framework.lang.CSProperty

interface CSActivityViewInterface {
    val onResume: CSEvent<Unit>
    val onPause: CSEvent<Unit>
    val onDestroy: CSEvent<Unit>
    val onBack: CSEvent<CSProperty<Boolean>>
    val onViewVisibilityChanged: CSEvent<Boolean>
    fun activity(): CSActivity
}