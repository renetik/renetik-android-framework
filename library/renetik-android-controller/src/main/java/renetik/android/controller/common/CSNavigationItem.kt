package renetik.android.controller.common

import renetik.android.controller.common.CSNavigationAnimation.SlideInRight
import renetik.android.controller.common.CSNavigationAnimation.SlideOutLeft
import renetik.android.core.lang.CSValue
import renetik.android.core.lang.CSValue.Companion.value

interface CSNavigationItem {
    val isNavigationBackPressedAllowed get() = true
    val isFullscreenNavigationItem: CSValue<Boolean> get() = value(true)
    val isBarVisible: Boolean? get() = null
    val isNavigationIconVisible: Boolean? get() = null
    val navigationItemIcon: Int? get() = null
    val isNavigationTitleVisible: Boolean? get() = null
    val navigationItemTitle: String? get() = null
    val isNavigationBackButtonVisible get() = true
    val navigationBackButtonIcon: Int? get() = null
    val navigationBackButtonIconTint: Int? get() = null

    val pushAnimation: CSNavigationAnimation get() = SlideInRight
    val popAnimation: CSNavigationAnimation get() = SlideOutLeft

    fun onViewControllerPush(navigation: CSNavigationView) = Unit
    fun onViewControllerPop(navigation: CSNavigationView) = Unit
}