package renetik.android.sample.view

import renetik.android.view.CSNavigationController
import renetik.android.view.base.CSActivity
import renetik.android.view.base.CSViewController

lateinit var navigation: SampleNavigationController

class SampleNavigationController(activity: CSActivity) : CSNavigationController(activity) {
    override fun onViewShowingFirstTime() {
        navigation.push(SampleListController())
    }
}

class NavigationActivity : CSActivity() {
    override fun createController(): CSViewController<*> {
        navigation = SampleNavigationController(this)
        return navigation
    }
}