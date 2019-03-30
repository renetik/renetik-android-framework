package renetik.android.sample.view

import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationController
import renetik.android.dialog.CSDialog
import renetik.android.sample.R
import renetik.android.themes.CSThemes

lateinit var navigation: CSNavigationController

fun CSViewController<*>.push() = navigation.push(this)

class SampleNavigation(activity: CSActivity) : CSNavigationController(activity) {
    override fun onViewShowingFirstTime() {
        navigation = this
        push(SampleMainController())
    }
}

class SampleNavigationActivity : CSActivity() {
    override fun createController(): CSNavigationController {
        CSThemes.initializeThemes(this)
        CSDialog.defaults { withIcon(R.drawable.om_black_196) }
        return SampleNavigation(this)
    }
}

