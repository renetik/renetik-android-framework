package renetik.android.sample.view

import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController

lateinit var navigation: SampleNavigationController

class SampleNavigationController(activity: CSActivity) : CSNavigationController(activity) {
    override fun onViewShowingFirstTime() {
        navigation = this
        SampleMainMenuController().push()
    }
}

class NavigationActivity : CSActivity() {
    override fun createController(): CSViewController<*> = SampleNavigationController(this)
}

fun CSViewController<*>.push() = apply { navigation.push(this) }