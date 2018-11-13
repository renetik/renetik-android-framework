package renetik.android.sample.view

import renetik.android.view.CSNavigationController
import renetik.android.viewbase.CSActivity
import renetik.android.viewbase.CSViewController

lateinit var navigation: SampleNavigationController

class SampleNavigationController(activity: NavigationActivity) : CSNavigationController(activity) {

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
//        navigation.push(SampleListController())
//        requestDriverApplicationPermissions()
    }

    override fun onResume() {
        super.onResume()
        navigation.push(SampleListController())
    }

//    private fun requestDriverApplicationPermissions() {
//        requestPermissions(list(CAMERA, ACCESS_FINE_LOCATION), { navigation.push(SampleListController()) }) {
//            dialog("Application cannot work without this permissions",
//                    "You need to allow application to use this permissions to work or it will exit")
//                    .show("Allow permissions", { requestDriverApplicationPermissions() },
//                            "Exit", { activity().finish() })
//        }
//    }
}

class NavigationActivity : CSActivity() {
    override fun createController(): CSViewController<*> {
        navigation = SampleNavigationController(this)
        return navigation
    }
}