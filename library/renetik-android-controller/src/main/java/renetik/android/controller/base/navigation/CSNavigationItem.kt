package renetik.android.controller.base.navigation

import renetik.android.controller.base.navigation.CSNavigationAnimation.SlideInRight
import renetik.android.controller.base.navigation.CSNavigationAnimation.SlideOutLeft
import renetik.android.core.lang.value.CSValue
import renetik.android.core.lang.value.CSValue.Companion.value

interface CSNavigationItem {
    val isNavigationBackPressedAllowed get() = true
    val isFullscreenNavigationItem: CSValue<Boolean> get() = value(true)
    val pushAnimation: CSNavigationAnimation get() = SlideInRight
    val popAnimation: CSNavigationAnimation get() = SlideOutLeft
    fun onViewControllerPush(navigation: CSNavigationView) = Unit
    fun onViewControllerPop(navigation: CSNavigationView) = Unit
}