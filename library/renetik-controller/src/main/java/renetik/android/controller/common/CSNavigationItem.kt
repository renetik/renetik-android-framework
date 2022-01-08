package renetik.android.controller.common

import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.CSValue.Companion.value

interface CSNavigationItem {
    val isFullscreenNavigationItem: CSValue<Boolean> get() = value(true)
    val isBarVisible: Boolean? get() = null
    val isNavigationIconVisible: Boolean? get() = null
    val navigationItemIcon: Int? get() = null
    val isNavigationTitleVisible: Boolean? get() = null
    val navigationItemTitle: String? get() = null
    val isNavigationBackButtonVisible get() = true
    val navigationBackButtonIcon: Int? get() = null
    val navigationBackButtonIconTint: Int? get() = null
    val pushAnimation: CSNavigationAnimation get() = CSNavigationAnimation.SlideInRight
    val popAnimation: CSNavigationAnimation get() = CSNavigationAnimation.SlideOutLeft
}