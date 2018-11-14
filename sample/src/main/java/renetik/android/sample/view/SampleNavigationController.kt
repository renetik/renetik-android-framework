package renetik.android.sample.view

import androidx.appcompat.app.AppCompatActivity
import renetik.android.view.CSNavigationController
import renetik.android.viewbase.CSActivity
import renetik.android.viewbase.CSViewController

lateinit var navigation: SampleNavigationController

class SampleNavigationController(activity: AppCompatActivity) : CSNavigationController(activity) {
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