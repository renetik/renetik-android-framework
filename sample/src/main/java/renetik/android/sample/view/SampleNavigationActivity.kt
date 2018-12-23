package renetik.android.sample.view

import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController

lateinit var navigation: CSNavigationController
fun CSViewController<*>.push() = navigation.push(this)

class SampleNavigationActivity : CSActivity() {
    override fun createController() = object : CSNavigationController(this) {
        override fun onViewShowingFirstTime() {
            navigation = this
            SampleMainMenuController().push()
        }
    }
}

