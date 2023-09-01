package renetik.android.controller.navigation

import renetik.android.controller.navigation.CSNavigationAnimation.FadeIn
import renetik.android.controller.navigation.CSNavigationAnimation.FadeOut
import renetik.android.core.lang.value.CSValue
import renetik.android.core.lang.value.CSValue.Companion.value

interface CSNavigationItem {
    val isNavigationBackPressedAllowed get() = true
    val isFullscreenNavigationItem: CSValue<Boolean> get() = value(true)
    val pushAnimation: CSNavigationAnimation get() = FadeIn
    val popAnimation: CSNavigationAnimation get() = FadeOut
    fun onViewControllerPush(navigation: CSNavigationView) = Unit
    fun onViewControllerPop(navigation: CSNavigationView) = Unit
}