package renetik.android.controller.navigation

import renetik.android.controller.navigation.CSNavigationAnimation.FadeIn
import renetik.android.controller.navigation.CSNavigationAnimation.FadeOut

interface CSNavigationItem {
    val isBackNavigationAllowed get() = true
    val isFullScreen: Boolean get() = true
    val pushAnimation: CSNavigationAnimation get() = FadeIn
    val popAnimation: CSNavigationAnimation get() = FadeOut
    fun onViewControllerPush(navigation: CSNavigation) = Unit
    fun onViewControllerPop(navigation: CSNavigation) = Unit
}